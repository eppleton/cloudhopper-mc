package com.cloudhopper.mc.provider.gcp;

/*-
 * #%L
 * generator-gcp-terraform - a library from the "Cloudhopper" project.
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
import com.cloudhopper.mc.ApiOperation;
import com.cloudhopper.mc.ApiOperation.ApiOperationFeature;
import com.cloudhopper.mc.Function;
import com.cloudhopper.mc.Function.FunctionAttribute;
import com.cloudhopper.mc.Schedule;
import com.cloudhopper.mc.Schedule.ScheduleAttribute;
import com.cloudhopper.mc.deployment.config.api.GenerationPhase;
import com.cloudhopper.mc.deployment.config.api.GenericDeploymentConfigGenerator.Template;
import com.cloudhopper.mc.deployment.config.api.GenericDeploymentConfigGenerator.TemplateRegistration;
import com.cloudhopper.mc.generator.features.GeneratorFeature;
import com.cloudhopper.mc.generator.features.GeneratorFeatures;

@GeneratorFeatures(
        generatorId = "gcp-terraform-java21",
        supportedFeatures = {
            @GeneratorFeature(
                    supportedAnnotation = Function.class,
                    supportedAttributes = {
                        FunctionAttribute.NAME,
                        FunctionAttribute.MEMORY,
                        FunctionAttribute.MIN_INSTANCES,
                        FunctionAttribute.TIMEOUT
                    }
            ),
            @GeneratorFeature(
                    supportedAnnotation = Schedule.class,
                    supportedAttributes = {ScheduleAttribute.CRON}
            ),
            @GeneratorFeature(
                    supportedAnnotation = ApiOperation.class,
                    supportedAttributes = {
                        ApiOperationFeature.SUMMARY, 
                        ApiOperationFeature.DESCRIPTION, 
                        ApiOperationFeature.OPERATION_ID,
                        ApiOperationFeature.PATH,
                        ApiOperationFeature.METHOD,
                        ApiOperationFeature.PARAMETERS
                    }
            )
        }
)
@TemplateRegistration(
        generatorId = "gcp-terraform-java21",
        templates = {
            @Template(
                    phase = GenerationPhase.SHARED,
                    templateName = "shared.ftl",
                    description = "Generates shared resources (e.g., provider blocks, logging config)",
                    outputFileExtension = "tf",
                    outputSubDirectory = "",
                    javaFile = false
            ),
            @Template(
                    phase = GenerationPhase.FUNCTION,
                    templateName = "handler.ftl",
                    description = "Generates the Java wrapper for the Lambda handler",
                    outputFileExtension = "java",
                    outputSubDirectory = "generated",
                    javaFile = true
            ),
            @Template(
                    phase = GenerationPhase.FUNCTION,
                    templateName = "function.ftl",
                    description = "Generates the terraform registration for the Lambda handler",
                    outputFileExtension = "tf",
                    outputSubDirectory = "",
                    javaFile = false
            ),
//            @Template(
//                    phase = GenerationPhase.API,
//                    templateName = "apiIntegrationClass.ftl",
//                    description = "Generates an APIFunction",
//                    outputFileExtension = "java",
//                    outputSubDirectory = "",
//                    javaFile = true
//            ),
            @Template(
                    phase = GenerationPhase.API,
                    templateName = "apiIntegration.ftl",
                    description = "Generates a openapi 2.0 snippet",
                    outputFileExtension = "json",
                    outputSubDirectory = "api-routes/",
                    javaFile = false
            ),
            @Template(
                    phase = GenerationPhase.SCHEDULE,
                    templateName = "schedule.ftl",
                    description = "Generates a google_cloud_scheduler_job resource",
                    outputFileExtension = "tf",
                    outputSubDirectory = "",
                    javaFile = false
            ),
            @Template(
                    phase = GenerationPhase.FINALIZE,
                    templateName = "api.ftl",
                    description = "Generates a google_api_gateway_api resource",
                    outputFileExtension = "tf",
                    outputSubDirectory = "",
                    javaFile = false
            )
        }
)
public class GcpTerraformJava21TemplateRegistration {
    // This class is solely for template registration
}
