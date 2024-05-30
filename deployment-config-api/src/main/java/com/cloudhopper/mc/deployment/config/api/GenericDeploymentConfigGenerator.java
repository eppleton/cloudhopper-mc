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
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.swagger.v3.oas.annotations.Operation;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;

public class GenericDeploymentConfigGenerator implements DeploymentConfigGenerator {

    private final Configuration freemarkerConfig;
    private final ProcessingEnvironment processingEnv;
    private static final Set<TemplateDescriptor> REQUIRED_TEMPLATES = new HashSet<>();

    static {
        REQUIRED_TEMPLATES.add(new TemplateDescriptor("function.ftl", "tf", "", false));
        REQUIRED_TEMPLATES.add(new TemplateDescriptor("handler.ftl", "java", "generated-sources", true));
    }

    public GenericDeploymentConfigGenerator(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;
        freemarkerConfig = new Configuration(Configuration.VERSION_2_3_32);
    }

    protected String getTemplateDirectory(String providerName) {
        return "/templates/" + providerName.toLowerCase();
    }

    @Override
    public void generateConfig(String providerName, String configOutputDir, HandlerInfo handlerInfo) throws ConfigGenerationException {
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), getTemplateDirectory(providerName));

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("functionId", handlerInfo.getFunctionId());
        dataModel.put("handler", handlerInfo.getHandlerClassName());
        dataModel.put("handlerImport", handlerInfo.getHandlerFullyQualifiedName());
        dataModel.put("package", handlerInfo.getHandlerPackage());
        dataModel.put("inputType", handlerInfo.getInputType());
        dataModel.put("outputType", handlerInfo.getOutputType());
        try {
            for (TemplateDescriptor templateDescriptor : REQUIRED_TEMPLATES) {
                if (templateDescriptor.isJavaFile()) {
                    generateJavaFile(templateDescriptor, dataModel, handlerInfo);
                } else {
                    renderTemplate(templateDescriptor, configOutputDir, dataModel);
                }
            }
        } catch (ConfigGenerationException e) {
            throw new ConfigGenerationException("Failed to generate config for provider: " + providerName, e);
        }
    }

    protected void renderTemplate(TemplateDescriptor templateDescriptor, String outputDirName, Map<String, Object> dataModel) throws ConfigGenerationException {
        try {
            Template template = freemarkerConfig.getTemplate(templateDescriptor.getTemplateName());
            StringWriter writer = new StringWriter();
            template.process(dataModel, writer);
            String output = writer.toString();

            Path outputDir = Path.of(outputDirName, templateDescriptor.getOutputSubDirectory());
            Files.createDirectories(outputDir);

            String outputFileName = templateDescriptor.getTemplateName().replace(".ftl", "." + templateDescriptor.getOutputFileExtension());
            Path outputPath = outputDir.resolve(outputFileName);

            try (FileWriter fileWriter = new FileWriter(outputPath.toFile())) {
                fileWriter.write(output);
            }
        } catch (IOException | TemplateException e) {
            throw new ConfigGenerationException("Failed to render template: " + templateDescriptor.getTemplateName(), e);
        }
    }

    protected void generateJavaFile(TemplateDescriptor templateDescriptor, Map<String, Object> dataModel, HandlerInfo handlerInfo) throws ConfigGenerationException {
        System.err.println("generate java file");
        try {
            Template template = freemarkerConfig.getTemplate(templateDescriptor.getTemplateName());
            StringWriter writer = new StringWriter();
            template.process(dataModel, writer);
            String fileContent = writer.toString();

            createClassFile(handlerInfo.getHandlerPackage(), extractClassName(fileContent), fileContent);
        } catch (IOException | TemplateException e) {
            throw new ConfigGenerationException("Failed to generate Java file from template: " + templateDescriptor.getTemplateName(), e);
        }
    }

    protected void createClassFile(String packageName, String fileName, String fileContent) {
        JavaFileObject builderFile;
        try {
            builderFile = processingEnv.getFiler().createSourceFile(packageName+"."+fileName);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return;
        }

        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.println(fileContent);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private static String extractClassName(String fileContent) {

        CompilationUnit cu = StaticJavaParser.parse(fileContent);
        List<ClassOrInterfaceDeclaration> classes = cu.findAll(ClassOrInterfaceDeclaration.class);
        if (!classes.isEmpty()) {
            return classes.get(0).getNameAsString();
        }
       return "UnknownClassName";
 
    }

    public boolean supportsProvider(String providerName) {
        String templateDir = getTemplateDirectory(providerName);

        List<String> validationErrors = TemplateValidator.validateTemplates(getClass().getClassLoader(), templateDir, REQUIRED_TEMPLATES);
        if (!validationErrors.isEmpty()) {
            validationErrors.forEach(System.err::println);
            return false;
        }

        return true;
    }
}
