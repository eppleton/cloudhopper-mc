/*
 * Copyright (C) 2025 antonepple
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.cloudhopper.mc.deployment.config.api;

/*-
 * #%L
 * deployment-config-api - a library from the "Cloudhopper" project.
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;

/**
 *
 * @author antonepple
 */
public class GeneratorProperties extends Properties{

    public GeneratorProperties(ProcessingEnvironment processingEnv, String file) {
        loadGeneratorProperties(processingEnv, file);
    }
    
    private void loadGeneratorProperties(ProcessingEnvironment processingEnv, String file) {
        String expectedId = processingEnv.getOptions().getOrDefault("generatorId", "").trim();
        if (expectedId.isEmpty()) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Missing required compiler arg: -AgeneratorId=<id>");
            return;
        }

        List<URL> matchingResources = new ArrayList<>();

        try {
            Enumeration<URL> resources = GeneratorProperties.class.getClassLoader()
                    .getResources(file);
            if (!resources.hasMoreElements()){
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, 
                        "Missing resource "+file);
            }
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                try (InputStream in = url.openStream()) {
                    load(in);
                    String id = getProperty("generator.id", "").trim();
                    System.err.println("generator.id="+id);
                    if (expectedId.equals(id)) {
                        matchingResources.add(url);
                    }
                }
            }

            if (matchingResources.isEmpty()) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "No "+file+" found matching generator.id=" + expectedId);
            } else if (matchingResources.size() > 1) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "Multiple "+file+" found for generator.id=" + expectedId + ":\n"
                        + matchingResources.stream().map(URL::toString).collect(Collectors.joining("\n")));
            } else {
                // Exactly one match, load and apply it
                URL matchedUrl = matchingResources.get(0);
                try (InputStream in = matchedUrl.openStream()) {
                    load(in);
                }
            }

        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "Error loading generator.properties: " + e.getMessage());
        }
    }

    
}
