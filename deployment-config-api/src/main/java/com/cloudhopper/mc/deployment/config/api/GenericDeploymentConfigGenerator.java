package com.cloudhopper.mc.deployment.config.api;

/*-
 * #%L
 * deployment-config-spi - a library from the "Cloudhopper" project.
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
import com.cloudhopper.mc.deployment.config.impl.TemplateValidator;
import com.cloudhopper.mc.deployment.config.spi.DeploymentConfigGenerator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;

public class GenericDeploymentConfigGenerator implements DeploymentConfigGenerator {

    private final ProcessingEnvironment processingEnv;
    private static final Set<TemplateDescriptor> REQUIRED_TEMPLATES = new LinkedHashSet<>();
    private final TemplateRenderer templateRenderer;
    protected static final TemplateDescriptor HANDLER_TEMPLATE_DESCRIPTOR = new TemplateDescriptor("handler.ftl", "java", "generated-sources", true);
    protected static final TemplateDescriptor FUNCTION_TEMPLATE_DESCRIPTOR = new TemplateDescriptor("function.ftl", "tf", "", false);
    protected static final TemplateDescriptor SHARED_RESOURCES_TEMPLATE_DESCRIPTOR = new TemplateDescriptor("shared.ftl", "tf", "", false);
    private boolean sharedConfigGenerated;
    static {
        REQUIRED_TEMPLATES.add(HANDLER_TEMPLATE_DESCRIPTOR);
        REQUIRED_TEMPLATES.add(FUNCTION_TEMPLATE_DESCRIPTOR);
    }

    public GenericDeploymentConfigGenerator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        this.templateRenderer = new TemplateRenderer();
        this.sharedConfigGenerated = false;
    }

    @Override
    public void generateConfig(String providerName, String configOutputDir, HandlerInfo handlerInfo) throws ConfigGenerationException {
        templateRenderer.setClassForTemplateLoading(this.getClass(), getTemplateDirectory(providerName));

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("functionId", handlerInfo.getFunctionId());
        dataModel.put("handler", handlerInfo.getHandlerClassName());
        dataModel.put("handlerFullyQualifiedName", handlerInfo.getHandlerFullyQualifiedName());

        dataModel.put("package", handlerInfo.getHandlerPackage());
        dataModel.put("inputType", handlerInfo.getInputType());
        dataModel.put("outputType", handlerInfo.getOutputType());
        dataModel.put("inputTypeImport", handlerInfo.getInputTypeImport());
        dataModel.put("outputTypeImport", handlerInfo.getOutputTypeImport());
        dataModel.put("version", handlerInfo.getVersion());
        dataModel.put("artifactId", handlerInfo.getArtifactId());
        dataModel.put("classifier", handlerInfo.getClassifier());
        dataModel.put("targetDir", handlerInfo.getTargetDir());
        try {
            if (!sharedConfigGenerated){
                sharedConfigGenerated = generateSharedConfig(providerName, configOutputDir, dataModel, handlerInfo);
            }
            templateRenderer.generateJavaFile(processingEnv, HANDLER_TEMPLATE_DESCRIPTOR, dataModel, handlerInfo);
            dataModel.put("handlerWrapperFullyQualifiedName", handlerInfo.getWrapperFullyQualifiedName());
            templateRenderer.renderTemplate(FUNCTION_TEMPLATE_DESCRIPTOR, configOutputDir, dataModel, handlerInfo.getFunctionId());
        } catch (ConfigGenerationException e) {
            throw new ConfigGenerationException("Failed to generate config for provider: " + providerName, e);
        }
    }
    
    private boolean generateSharedConfig(String providerName, String configOutputDir, Map<String, Object> dataModel, HandlerInfo handlerInfo) {
        try {
            templateRenderer.renderTemplate(SHARED_RESOURCES_TEMPLATE_DESCRIPTOR, configOutputDir, dataModel, "shared-resources");
        } catch (ConfigGenerationException e) { // shared config is optional, so we catch the exception and only print a warning
            processingEnv.getMessager().printWarning("Could not generate shared config " +e.getMessage());
        }
        return true;
    }

    @Override
    public boolean supportsProvider(String providerName) {
        String templateDir = getTemplateDirectory(providerName);

        List<String> validationErrors = TemplateValidator.validateTemplates(getClass().getClassLoader(), templateDir, REQUIRED_TEMPLATES);
        if (!validationErrors.isEmpty()) {
            validationErrors.forEach(System.err::println);
            return false;
        }

        return true;
    }

    protected String getTemplateDirectory(String providerName) {
        return "/templates/" + providerName.toLowerCase();
    }
}
