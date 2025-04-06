package com.cloudhopper.mc.deployment.config.generator;

/*-
 * #%L
 * deployment-config-generator - a library from the "Cloudhopper" project.
 * 
 * Eppleton IT Consulting designates this particular file as subject to the "Classpath"
 * exception as provided in the README.md file that accompanies this code.
 * %%
 * Copyright (C) 2024 Eppleton IT Consulting
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
// Annotation Processor
import com.cloudhopper.mc.ApiOperation;
import com.cloudhopper.mc.Function;
import com.cloudhopper.mc.Schedule;
import com.cloudhopper.mc.deployment.config.api.CloudRequestHandler;
import com.cloudhopper.mc.deployment.config.api.ConfigGenerationException;
import com.cloudhopper.mc.deployment.config.api.HandlerInfo;
import com.google.auto.service.AutoService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.cloudhopper.mc.Function")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class ServerlessFunctionProcessor extends BaseDeploymentInfoProcessor {

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        AnnotationFeatureValidator featureValidator = new AnnotationFeatureValidator(processingEnv, generatorFeatureInfo);

        if (roundEnv.processingOver()) {
            try {
                deploymentGenerator.finalizeConfig(generatorID, configOutputDir);
            } catch (ConfigGenerationException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                MessagerUtil.printExceptionStackTrace(processingEnv.getMessager(), e);
            }
        } else {
            for (Element element : roundEnv.getElementsAnnotatedWith(Function.class)) {
                if (element.getKind() == ElementKind.METHOD) {
                    ExecutableElement methodElement = (ExecutableElement) element;
                    TypeElement classElement = (TypeElement) methodElement.getEnclosingElement();

                    if (!implementsInterface(classElement, CloudRequestHandler.class.getName())) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                                "Class " + classElement.getQualifiedName() + " must implement " + CloudRequestHandler.class.getName(),
                                classElement);
                        return true;
                    }

                    // Extract core annotation
                    Function functionAnnotation = methodElement.getAnnotation(Function.class);
                    ApiOperation apiOperation = methodElement.getAnnotation(ApiOperation.class);
                    Schedule schedule = methodElement.getAnnotation(Schedule.class);

                    String methodName = methodElement.getSimpleName().toString();
                    String handlerFQN = classElement.getQualifiedName().toString();
                    String packageName = handlerFQN.substring(0, handlerFQN.lastIndexOf('.'));
                    String handlerSimpleName = classElement.getSimpleName().toString();
                    TypeMirror inputType = methodElement.getParameters().isEmpty() ? null : methodElement.getParameters().get(0).asType();
                    TypeMirror outputType = methodElement.getReturnType();

                    final HandlerInfo handlerInfo = new HandlerInfo(
                            functionAnnotation.name(),
                            functionAnnotation.memory(),
                            functionAnnotation.timeout(),
                            functionAnnotation.minInstances(),
                            handlerSimpleName,
                            handlerFQN,
                            packageName,
                            methodName,
                            inputType != null ? inputType.toString() : "void",
                            outputType.toString(),
                            artifactId,
                            version,
                            classifier,
                            targetDir
                    );

                    try {
                        deploymentGenerator.generateServerlessFunctionConfiguration(generatorID, configOutputDir, handlerInfo, processingEnv);

                        if (apiOperation != null) {
                            deploymentGenerator.generateApiResourceAndIntegration(generatorID, configOutputDir, handlerInfo, apiOperation, processingEnv);
                        }

                        if (schedule != null) {
                            deploymentGenerator.generateScheduledTrigger(generatorID, configOutputDir, handlerInfo, schedule, processingEnv);
                        }
                        featureValidator.validate(methodElement);
                    } catch (ConfigGenerationException e) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                        MessagerUtil.printExceptionStackTrace(processingEnv.getMessager(), e);
                    }
                }
            }
        }
        return true;
    }

    private String[] extractPathParams(String path) {
        List<String> pathParamsList = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\{([^}]+)}");
        Matcher matcher = pattern.matcher(path);

        while (matcher.find()) {
            pathParamsList.add(matcher.group(1));
        }

        return pathParamsList.toArray(new String[0]);
    }

}
