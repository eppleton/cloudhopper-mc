package com.cloudhopper.mc.generator.springboot;

/*-
 * #%L
 * generator-springboot - a library from the "Cloudhopper" project.
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

import com.cloudhopper.mc.annotations.ApiOperation;
import com.cloudhopper.mc.annotations.ApiOperation.ApiOperationAttribute;
import com.cloudhopper.mc.annotations.Function;
import com.cloudhopper.mc.annotations.Function.FunctionAttribute;
import com.cloudhopper.mc.generator.api.GenerationPhase;
import com.cloudhopper.mc.generator.api.annotations.GeneratorFeature;
import com.cloudhopper.mc.generator.api.annotations.GeneratorFeatures;
import com.cloudhopper.mc.generator.generic.annotations.Template;
import com.cloudhopper.mc.generator.generic.annotations.TemplateRegistration;

@GeneratorFeatures(
    generatorId = "springboot-http",
    supportedFeatures = {
        @GeneratorFeature(
            supportedAnnotation = Function.class,
            supportedAttributes = {
                FunctionAttribute.NAME
            }
        ),
        @GeneratorFeature(
            supportedAnnotation = ApiOperation.class,
            supportedAttributes = {
                ApiOperationAttribute.SUMMARY,
                ApiOperationAttribute.DESCRIPTION,
                ApiOperationAttribute.OPERATION_ID,
                ApiOperationAttribute.PATH,
                ApiOperationAttribute.METHOD,
                ApiOperationAttribute.PARAMETERS
            }
        )
    }
)
@TemplateRegistration(
    generatorId = "springboot-http",
    templates = {
        @Template(
            phase = GenerationPhase.API,
            templateName = "handler.ftl",
            description = "Generates Spring Boot REST controller for API functions",
            outputFileExtension = "java",
            outputSubDirectory = "generated",
            javaFile = true
        ),
        @Template(
            phase = GenerationPhase.SHARED,
            templateName = "dockerfile.ftl",
            description = "Generates a Dockerfile for the Spring Boot app",
            outputFileExtension = ".dockerfile",
            outputSubDirectory = "",
            javaFile = false
        )
    }
)
public class SpringBootTemplateRegistration {
}
