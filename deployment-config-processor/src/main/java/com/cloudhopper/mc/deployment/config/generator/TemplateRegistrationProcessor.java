package com.cloudhopper.mc.deployment.config.generator;

/*-
 * #%L
 * deployment-config-generator - a library from the "Cloudhopper" project.
 * 
 * Eppleton IT Consulting designates this particular file as subject to the "Classpath"
 * exception as provided in the README.md file that accompanies this code.
 * %%
 * Copyright (C) 2024 - 2025 Eppleton IT Consulting
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


import com.cloudhopper.mc.deployment.config.generator.generic.GeneratorConfig;
import com.cloudhopper.mc.deployment.config.generator.generic.GenericDeploymentConfigGenerator.Template;
import com.cloudhopper.mc.deployment.config.generator.generic.GenericDeploymentConfigGenerator.TemplateRegistration;
import com.cloudhopper.mc.deployment.config.generator.generic.TemplateDescriptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.lang.model.SourceVersion;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AutoService(javax.annotation.processing.Processor.class)
@SupportedAnnotationTypes("com.cloudhopper.mc.deployment.config.generator.generic.GenericDeploymentConfigGenerator.TemplateRegistration")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class TemplateRegistrationProcessor extends AbstractProcessor {

    private ObjectMapper objectMapper;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(TemplateRegistration.class)) {
            TemplateRegistration registration = element.getAnnotation(TemplateRegistration.class);
            if (registration == null) {
                continue;
            }
            
            String generatorId = registration.generatorId();
            Template[] templates = registration.templates();
            Map<String, List<TemplateDescriptor>> phaseMap = new HashMap<>();
            
            for (Template template : templates) {
                String phaseKey = template.phase().name().toLowerCase();
                TemplateDescriptor descriptor = new TemplateDescriptor(
                        template.templateName(),
                        template.description(),
                        template.outputFileExtension(),
                        template.outputSubDirectory(),
                        template.javaFile()
                );
                
                phaseMap.computeIfAbsent(phaseKey, k -> new ArrayList<>()).add(descriptor);
            }
            
            GeneratorConfig config = new GeneratorConfig();
            config.setGeneratorId(generatorId);
            config.setPhases(phaseMap);

            String jsonOutput;
            try {
                jsonOutput = objectMapper.writeValueAsString(config);
            } catch (Exception e) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        "Error serializing generator configuration for " + generatorId + ": " + e.getMessage(),
                        element
                );
                continue;
            }
            
            Filer filer = processingEnv.getFiler();
            String resourceFile = "META-INF/cloudhopper/" + generatorId + "-templates.json";
            try {
                FileObject fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT, "", resourceFile, element);
                try (Writer writer = fileObject.openWriter()) {
                    writer.write(jsonOutput);
                }
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                        "Generated " + resourceFile);
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        "Error writing generator configuration file for " + generatorId + ": " + e.getMessage(),
                        element
                );
            }
        }
        return true; // Keine weiteren Prozessoren sollen diese Annotationen verarbeiten.
    }
}
