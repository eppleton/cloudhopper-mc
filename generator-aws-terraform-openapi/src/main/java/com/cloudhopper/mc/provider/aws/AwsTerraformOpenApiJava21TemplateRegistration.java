package com.cloudhopper.mc.provider.aws;

/*-
 * #%L
 * generator-aws-terraform-openapi - a library from the "Cloudhopper" project.
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
import com.cloudhopper.mc.Function;
import com.cloudhopper.mc.Schedule;
import com.cloudhopper.mc.Schedule.ScheduleAttribute;
import com.cloudhopper.mc.deployment.config.api.GenerationPhase;
import com.cloudhopper.mc.deployment.config.api.GenericDeploymentConfigGenerator.Template;
import com.cloudhopper.mc.deployment.config.api.GenericDeploymentConfigGenerator.TemplateRegistration;
import com.cloudhopper.mc.generator.features.GeneratorFeature;
import com.cloudhopper.mc.generator.features.GeneratorFeatures;

@GeneratorFeatures(
        generatorId = "aws-terraform-openapi-java21",
        supportedFeatures = {
            @GeneratorFeature(
                    supportedAnnotation = Function.class,
                    supportedAttributes = {
                        Function.FunctionAttribute.NAME,
                        Function.FunctionAttribute.MEMORY,
                        Function.FunctionAttribute.MIN_INSTANCES,
                        Function.FunctionAttribute.TIMEOUT
                    }
            ),
            @GeneratorFeature(
                    supportedAnnotation = Schedule.class,
                    supportedAttributes = {
                        ScheduleAttribute.CRON,
                        ScheduleAttribute.TIMEZONE
                    }
            ),
            @GeneratorFeature(
                    supportedAnnotation = ApiOperation.class,
                    supportedAttributes = {
                        ApiOperation.ApiOperationFeature.SUMMARY,
                        ApiOperation.ApiOperationFeature.DESCRIPTION,
                        ApiOperation.ApiOperationFeature.OPERATION_ID,
                        ApiOperation.ApiOperationFeature.PATH,
                        ApiOperation.ApiOperationFeature.METHOD,
                        ApiOperation.ApiOperationFeature.PARAMETERS
                    }
            )
        }
)
@TemplateRegistration(
        generatorId = "aws-terraform-openapi-java21",
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
                    description = "Generates AWS Lambda function definition (Terraform)",
                    outputFileExtension = "tf",
                    outputSubDirectory = "",
                    javaFile = false
            ),
            @Template(
                    phase = GenerationPhase.API,
                    templateName = "integration.ftl",
                    description = "Generates route and integration for each Lambda function (Terraform)",
                    outputFileExtension = "tf",
                    outputSubDirectory = "",
                    javaFile = false
            ),
            @Template(
                    phase = GenerationPhase.FINALIZE,
                    templateName = "api.ftl",
                    description = "Generates the OpenAPI definition for AWS API Gateway",
                    outputFileExtension = "tf",
                    outputSubDirectory = "",
                    javaFile = false
            ),
            @Template(
                    phase = GenerationPhase.SCHEDULE,
                    templateName = "schedule.ftl",
                    description = "Generates AWS EventBridge/CloudWatch schedule rule (Terraform)",
                    outputFileExtension = "tf",
                    outputSubDirectory = "",
                    javaFile = false
            )
        }
)
public class AwsTerraformOpenApiJava21TemplateRegistration {
    // This class is solely for template registration
}
