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
import com.cloudhopper.mc.deployment.config.api.CloudRequestHandler;
import com.cloudhopper.mc.deployment.config.spi.DeploymentConfigGenerator;
import com.cloudhopper.mc.deployment.config.api.ConfigGenerationException;
import com.cloudhopper.mc.deployment.config.api.HandlerInfo;
import com.cloudhopper.mc.deployment.config.api.TemplateDescriptor;
import com.cloudhopper.mc.deployment.config.api.TemplateRenderer;
import com.google.auto.service.AutoService;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    
    private TemplateRenderer templateRenderer;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        templateRenderer = new TemplateRenderer();
        templateRenderer.setClassForTemplateLoading(ServerlessFunctionProcessor.class, "/templates");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (Element element : roundEnv.getElementsAnnotatedWith(Function.class)) {
            if (element.getKind() == ElementKind.METHOD) {
                ExecutableElement methodElement = (ExecutableElement) element;
                TypeElement classElement = (TypeElement) methodElement.getEnclosingElement();

                // Check if the class implements CloudRequestHandler
                if (!implementsInterface(classElement, CloudRequestHandler.class.getName())) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                            "Class " + classElement.getQualifiedName() + " must implement " + CloudRequestHandler.class.getName(),
                            classElement);
                    return true;
                }

                // Extract annotation information
                Function functionAnnotation = element.getAnnotation(Function.class);
                ApiOperation apiOperation = functionAnnotation.apiIntegration();

                // Extract method and class details
                String methodName = methodElement.getSimpleName().toString();
                String handlerFQN = classElement.getQualifiedName().toString();
                String packageName = handlerFQN.substring(0, handlerFQN.lastIndexOf('.'));
                String handlerSimpleName = classElement.getSimpleName().toString();
                TypeMirror inputType = methodElement.getParameters().isEmpty() ? null : methodElement.getParameters().get(0).asType();
                TypeMirror outputType = methodElement.getReturnType();

                try {
                    if (apiOperation.operationId().equals(Function.NO_OP_ID)) {
                        // Generate configuration for the function
                        deploymentGenerator.generateConfig(providerName, configOutputDir,
                                new HandlerInfo(
                                        functionAnnotation.name(),
                                        handlerSimpleName,
                                        handlerFQN,
                                        packageName,
                                        methodName,
                                        inputType.toString(),
                                        outputType.toString(),
                                        artifactId,
                                        version,
                                        classifier,
                                        targetDir
                                ));
                    } else {
                        // Generate the corresponding class with @Operation annotation
                        generateApiIntegrationClass(classElement, methodElement, functionAnnotation, apiOperation, inputType, outputType);
                    }
                } catch (ConfigGenerationException e) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
                    MessagerUtil.printExceptionStackTrace(processingEnv.getMessager(), e);
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

    private void generateApiIntegrationClass(TypeElement classElement, ExecutableElement methodElement, Function functionAnnotation, ApiOperation apiOperation, TypeMirror inputType, TypeMirror outputType) throws ConfigGenerationException {
        // Prepare data model for Freemarker template
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", getPackageName(classElement));
        dataModel.put("className", classElement.getSimpleName().toString() + "Api");
        dataModel.put("methodName", methodElement.getSimpleName().toString());
        dataModel.put("summary", apiOperation.summary());
        dataModel.put("description", apiOperation.description());
        dataModel.put("operationId", apiOperation.operationId());
        dataModel.put("path", apiOperation.path());
        dataModel.put("httpMethod", apiOperation.method().toUpperCase());
        dataModel.put("handlerClassName", classElement.getSimpleName().toString());
        dataModel.put("inputType", inputType.toString());
        dataModel.put("outputType", outputType.toString());
        // Extract path and query parameters
        String[] pathParams = extractPathParams(apiOperation.path());
        List<Map<String, String>> parameters = new ArrayList<>();
        for (String pathParam : pathParams) {
            Map<String, String> paramData = new HashMap<>();
            paramData.put("in", "PATH");
            paramData.put("name", pathParam);
            parameters.add(paramData);
        }

        for (Parameter param : apiOperation.parameters()) {
            Map<String, String> paramData = new HashMap<>();
            paramData.put("in", param.in().name());
            paramData.put("name", param.name());
            paramData.put("description", param.description());
            paramData.put("example", param.example());
            parameters.add(paramData);
        }

        dataModel.put("parameters", parameters);

        TemplateDescriptor templateDescriptor = new TemplateDescriptor("apiIntegrationClass.ftl", "", "java", true);

        templateRenderer.generateJavaFile(processingEnv, templateDescriptor, dataModel, new HandlerInfo(
                functionAnnotation.name(),
                classElement.getSimpleName().toString(),
                classElement.getQualifiedName().toString(),
                getPackageName(classElement),
                methodElement.getSimpleName().toString(),
                methodElement.getParameters().isEmpty() ? null : methodElement.getParameters().get(0).asType().toString(),
                methodElement.getReturnType().toString(), 
                artifactId,
                version,
                classifier,
                targetDir
        ));
    }

}
