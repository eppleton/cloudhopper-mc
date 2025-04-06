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
import com.cloudhopper.mc.generator.features.GeneratorFeature;
import com.cloudhopper.mc.generator.features.GeneratorFeatures;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.auto.service.AutoService;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.cloudhopper.mc.generator.features.GeneratorFeatures")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class GeneratorFeatureProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.getElementsAnnotatedWith(GeneratorFeatures.class).size() > 1) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    "Only one @GeneratorFeatures annotation is allowed per module."
            );
        }
        for (Element element : roundEnv.getElementsAnnotatedWith(GeneratorFeatures.class)) {
            GeneratorFeatures annotation = element.getAnnotation(GeneratorFeatures.class);
            if (annotation == null) {
                continue;
            }

            GeneratorFeatureInfo info = new GeneratorFeatureInfo();
            info.generatorId = annotation.generatorId();

            for (GeneratorFeature feature : annotation.supportedFeatures()) {
                GeneratorFeatureInfo.FeatureEntry entry = new GeneratorFeatureInfo.FeatureEntry();

                try {
                    entry.supportedAnnotation = feature.supportedAnnotation().getCanonicalName();
                } catch (MirroredTypeException e) {
                    entry.supportedAnnotation = e.getTypeMirror().toString();
                }

                entry.supportedAttributes = List.of(feature.supportedAttributes());
                info.features.add(entry);
            }

            writeJsonFile(info); // <--- direkt schreiben
        }

        return false;
    }

    private void writeJsonFile(GeneratorFeatureInfo info) {
        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        try {
            String filename = "META-INF/cloudhopper/" + info.generatorId + "-features.json";

            FileObject resource = processingEnv.getFiler().createResource(
                    StandardLocation.CLASS_OUTPUT,
                    "",
                    filename
            );

            try (Writer writer = resource.openWriter()) {
                objectMapper.writeValue(writer, info);
            }

        } catch (IOException e) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.ERROR,
                    "Failed to write generator features for " + info.generatorId + ": " + e.getMessage()
            );
        }
    }
}
