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
import com.cloudhopper.mc.deployment.config.api.ConfigGenerationException;
import com.cloudhopper.mc.deployment.config.api.HandlerInfo;
import com.google.auto.service.AutoService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Set;
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
@SupportedAnnotationTypes("io.swagger.v3.oas.annotations.Operation")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class OperationProcessor extends BaseDeploymentInfoProcessor {

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            try {
                deploymentGenerator.finalizeConfig(generatorID, configOutputDir);
            } catch (ConfigGenerationException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                MessagerUtil.printExceptionStackTrace(processingEnv.getMessager(), e);            }
        } else {
            for (Element element : roundEnv.getElementsAnnotatedWith(Operation.class)) {
                if (element.getKind() == ElementKind.METHOD) {
                    ExecutableElement methodElement = (ExecutableElement) element;
                    String methodName = methodElement.getSimpleName().toString();
                    String handlerFQN = ((TypeElement) methodElement.getEnclosingElement()).getQualifiedName().toString();
                    String packageName = handlerFQN.substring(0, handlerFQN.lastIndexOf('.'));
                    String handlerSimpleName = ((TypeElement) methodElement.getEnclosingElement()).getSimpleName().toString();
                    TypeMirror inputType = methodElement.getParameters().isEmpty() ? null : methodElement.getParameters().get(0).asType();
                    TypeMirror outputType = methodElement.getReturnType();
                    Operation operation = element.getAnnotation(Operation.class);
                    try {
                        deploymentGenerator.generateServerlessFunctionConfiguration(generatorID, configOutputDir,
                               
                                new HandlerInfo(operation.operationId(),
                                        handlerSimpleName,
                                        handlerFQN,
                                        packageName,
                                        methodName,
                                        inputType.toString(),
                                        outputType.toString(),
                                        artifactId,
                                        version,
                                        classifier,
                                        targetDir),
                                processingEnv
                        );
                    } catch (ConfigGenerationException e) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                        MessagerUtil.printExceptionStackTrace(processingEnv.getMessager(), e);
                    }
                }
            }
        }
        return true;
    }


}
