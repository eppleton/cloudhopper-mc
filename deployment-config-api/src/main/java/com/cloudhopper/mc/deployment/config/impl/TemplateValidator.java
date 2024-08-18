package com.cloudhopper.mc.deployment.config.impl;

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
import com.cloudhopper.mc.deployment.config.api.TemplateDescriptor;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TemplateValidator {
    public static List<String> validateTemplates(ClassLoader classLoader, String templateDir, Set<TemplateDescriptor> requiredTemplates) {
        return requiredTemplates.stream()
                .map(TemplateDescriptor::getTemplateName)
                .filter(templateName -> classLoader.getResource(templateDir + "/" + templateName) == null)
                .collect(Collectors.toList());
    }
}