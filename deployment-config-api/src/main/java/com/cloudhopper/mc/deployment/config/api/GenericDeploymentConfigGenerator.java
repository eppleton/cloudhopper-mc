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
import com.cloudhopper.mc.ApiOperation;
import com.cloudhopper.mc.Schedule;
import com.cloudhopper.mc.deployment.config.spi.DeploymentConfigGenerator;
import com.github.javaparser.quality.Nullable;
import io.swagger.v3.oas.annotations.Parameter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;

public class GenericDeploymentConfigGenerator implements DeploymentConfigGenerator {

    private static final String API_HANDLER_FILE = "handler-info.properties";
    private String GENERATOR_CONFIG_FILE = "META-INF/cloudhopper/generator-config.json";

    GeneratorConfig generatorConfig;

    private final TemplateRenderer templateRenderer;
    private final ProcessingEnvironment processingEnv;
    private boolean sharedConfigGenerated;

    public GenericDeploymentConfigGenerator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        this.templateRenderer = new TemplateRenderer();
        this.sharedConfigGenerated = false;
        generatorConfig = GeneratorConfigLoader.loadGeneratorConfig(processingEnv, GENERATOR_CONFIG_FILE);
    }

    @Override
    public boolean supportsGenerator(String generatorId) {
        return generatorConfig.getGeneratorId().equals(generatorId);
    }

    @Override
    public void generateServerlessFunctionConfiguration(String generatorId, String outputDir, HandlerInfo handlerInfo, ProcessingEnvironment env) throws ConfigGenerationException {
        templateRenderer.setClassForTemplateLoading(this.getClass(), getTemplateDirectory(generatorId));
        Map<String, Object> dataModel = createBaseDataModel(handlerInfo);
        try {
            if (!sharedConfigGenerated) {
                sharedConfigGenerated = generateSharedConfig(generatorId, outputDir, dataModel, handlerInfo);
            }
            runPhase(processingEnv, GenerationPhase.FUNCTION, dataModel, handlerInfo, outputDir, handlerInfo.getFunctionId().toLowerCase());
        } catch (ConfigGenerationException e) {
            throw new ConfigGenerationException("Failed to generate function config for generator: " + generatorId, e);
        }
    }

    @Override
    public void generateApiResourceAndIntegration(String generatorId, String outputDir, HandlerInfo handlerInfo, ApiOperation apiOperation, ProcessingEnvironment env) throws ConfigGenerationException {
        Map<String, Object> dataModel = getApiIntegrationDataModelForTemplateRendering(handlerInfo, apiOperation);
        templateRenderer.setClassForTemplateLoading(this.getClass(), getTemplateDirectory(generatorId));
        saveHandlerInfo(handlerInfo, outputDir);
        runPhase(processingEnv, GenerationPhase.API, dataModel, handlerInfo, outputDir, handlerInfo.getFunctionId().toLowerCase() + "_api");
    }

    @Override
    public void finalizeConfig(String providerName, String configOutputDir) throws ConfigGenerationException {
        templateRenderer.setClassForTemplateLoading(this.getClass(), getTemplateDirectory(providerName));

        Properties properties = loadProperties(configOutputDir);
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("lambdaMap", new HashMap<>(properties.stringPropertyNames().stream()
                .collect(Collectors.toMap(k -> k, properties::getProperty))));

        try {
            runPhase(processingEnv, GenerationPhase.FINALIZE, dataModel, null, configOutputDir, "api");
            copyDocumentationResources(providerName, configOutputDir);
        } catch (ConfigGenerationException e) {
            throw new ConfigGenerationException("Failed to finalize config for provider: " + providerName, e);
        }
    }

    protected boolean generateSharedConfig(String providerName, String configOutputDir, Map<String, Object> dataModel, HandlerInfo handlerInfo) {
        try {
            runPhase(processingEnv, GenerationPhase.SHARED, dataModel, handlerInfo, configOutputDir, "shared-resources");
        } catch (ConfigGenerationException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void generateScheduledTrigger(String generatorID, String configOutputDir, HandlerInfo handlerInfo, Schedule schedule, ProcessingEnvironment processingEnv) {
        templateRenderer.setClassForTemplateLoading(this.getClass(), getTemplateDirectory(generatorID));

        Map<String, Object> dataModel = createBaseDataModel(handlerInfo);
        dataModel.put("scheduleExpression", schedule.cron());
        dataModel.put("scheduleTimezone", schedule.timezone());

        try {
            runPhase(processingEnv, GenerationPhase.SCHEDULE, dataModel, handlerInfo, configOutputDir, handlerInfo.getFunctionId().toLowerCase() + "_schedule");
        } catch (ConfigGenerationException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Failed to generate schedule for function " + handlerInfo.getFunctionId().toLowerCase() + ": " + e.getMessage());
        }
    }

    private void runPhase(ProcessingEnvironment processingEnv,
            GenerationPhase phase,
            Map<String, Object> dataModel,
            @Nullable HandlerInfo handlerInfo,
            String outputDir,
            String fileName) throws ConfigGenerationException {
        System.err.println("Running generation phase " + phase.getId() + ": " + GenerationPhase.FUNCTION.getDescription());
        List<TemplateDescriptor> templates = generatorConfig.getPhases().get(phase.getId());
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Running generation phase " + phase.getId() + ": " + GenerationPhase.FUNCTION.getDescription());
        if (templates == null) {
            return; // No templates for this phase
        }
        for (TemplateDescriptor descriptor : templates) {
            if (descriptor.isJavaFile()) {
                if (handlerInfo == null) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                            "Cannot generate Java file in phase '" + phase.getId() + "' because there is no HandlerInfo.");
                    continue;
                }
                templateRenderer.generateJavaFile(processingEnv, descriptor, dataModel, handlerInfo);
                if (handlerInfo.getWrapperFullyQualifiedName() != null) {
                    dataModel.put("handlerWrapperFullyQualifiedName", handlerInfo.getWrapperFullyQualifiedName());
                }
            } else {
                templateRenderer.renderTemplate(descriptor, outputDir, dataModel, fileName);
            }
        }
    }

    protected Map<String, Object> createBaseDataModel(HandlerInfo handlerInfo) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("functionId", handlerInfo.getFunctionId().toLowerCase());
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
        return dataModel;
    }

    protected Map<String, Object> getApiIntegrationDataModelForTemplateRendering(HandlerInfo handlerInfo, ApiOperation apiOp) {
        Map<String, Object> dataModel = createBaseDataModel(handlerInfo);
        dataModel.put("packageName", handlerInfo.getHandlerPackage());
        dataModel.put("className", handlerInfo.getHandlerClassName() + "Api");
        dataModel.put("methodName", handlerInfo.getHandlerMethod());
        dataModel.put("summary", apiOp.summary());
        dataModel.put("description", apiOp.description());
        dataModel.put("operationId", apiOp.operationId());
        dataModel.put("path", apiOp.path());
        dataModel.put("httpMethod", apiOp.method().toUpperCase());
        dataModel.put("handlerClassName", handlerInfo.getHandlerClassName());
        dataModel.put("inputType", handlerInfo.getInputType());
        dataModel.put("outputType", handlerInfo.getOutputType());

        List<Map<String, String>> parameters = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\{([^}]+)}").matcher(apiOp.path());
        while (matcher.find()) {
            Map<String, String> param = new HashMap<>();
            param.put("in", "PATH");
            param.put("name", matcher.group(1));
            parameters.add(param);
        }

        for (Parameter param : apiOp.parameters()) {
            Map<String, String> paramData = new HashMap<>();
            paramData.put("in", param.in().name());
            paramData.put("name", param.name());
            paramData.put("description", param.description());
            paramData.put("example", param.example());
            parameters.add(paramData);
        }

        dataModel.put("parameters", parameters);
        return dataModel;
    }

    protected String getTemplateDirectory(String providerName) {
        return "/templates/" + providerName.toLowerCase();
    }

    private File getConfigFile(String configOutputDir) throws IOException {
        Path metaInfPath = Paths.get(configOutputDir, "META-INF");
        Files.createDirectories(metaInfPath);
        return new File(metaInfPath.toFile(), API_HANDLER_FILE);
    }

    private Properties loadProperties(String configOutputDir) {
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(getConfigFile(configOutputDir))) {
            properties.load(is);
        } catch (IOException ignored) {
        }
        return properties;
    }

    private void saveProperties(Properties properties, String configOutputDir) {
        try (OutputStream os = new FileOutputStream(getConfigFile(configOutputDir))) {
            properties.store(os, "Generated Handler Info");
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Error writing properties file: " + e.getMessage());
        }
    }

    private void saveHandlerInfo(HandlerInfo handlerInfo, String configOutputDir) {
        Properties properties = loadProperties(configOutputDir);
        properties.setProperty(handlerInfo.getHandlerClassName() + "_Arn", handlerInfo.getFunctionId().toLowerCase());
        saveProperties(properties, configOutputDir);
    }

    protected void copyDocumentationResources(String providerName, String outputDir) {
        String basePath = getTemplateDirectory(providerName) + "/doc/";
        try (InputStream indexStream = this.getClass().getResourceAsStream(basePath + "doc.index")) {
            if (indexStream == null) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
                        "No doc.index found — skipping documentation copy.");
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(indexStream));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                try (InputStream resourceStream = this.getClass().getResourceAsStream(basePath + line)) {
                    if (resourceStream == null) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                                "Missing documentation resource listed in doc.index: " + line);
                        continue;
                    }

                    Path targetPath = Paths.get(outputDir, line);
                    Files.createDirectories(targetPath.getParent());
                    Files.copy(resourceStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }

        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "Failed to copy documentation resources: " + e.getMessage());
        }
    }

}
