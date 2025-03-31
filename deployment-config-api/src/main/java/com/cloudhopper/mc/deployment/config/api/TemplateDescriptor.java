package com.cloudhopper.mc.deployment.config.api;

/*-
 * #%L
 * deployment-config-api - a library from the "Cloudhopper" project.
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

public class TemplateDescriptor {
    private String templateName;
    private String description;
    private String outputFileExtension;
    private String outputSubDirectory;
    private boolean javaFile;

    public TemplateDescriptor() {
        // Empty constructor for JSON deserialization
    }

    public TemplateDescriptor(String templateName,
                              String description,
                              String outputFileExtension,
                              String outputSubDirectory,
                              boolean javaFile) {
        this.templateName = templateName;
        this.description = description;
        this.outputFileExtension = outputFileExtension;
        this.outputSubDirectory = outputSubDirectory;
        this.javaFile = javaFile;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getDescription() {
        return description;
    }

    public String getOutputFileExtension() {
        return outputFileExtension;
    }

    public String getOutputSubDirectory() {
        return outputSubDirectory;
    }

    public boolean isJavaFile() {
        return javaFile;
    }

    @Override
    public String toString() {
        return "TemplateDescriptor{" +
                "templateName='" + templateName + '\'' +
                ", description='" + description + '\'' +
                ", outputFileExtension='" + outputFileExtension + '\'' +
                ", outputSubDirectory='" + outputSubDirectory + '\'' +
                ", javaFile=" + javaFile +
                '}';
    }
}