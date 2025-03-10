package com.cloudhopper.mc.demo;

/*-
 * #%L
 * demo - a library from the "Cloudhopper" project.
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
import com.cloudhopper.mc.deployment.config.api.CloudRequestHandler;
import com.cloudhopper.mc.deployment.config.api.HandlerContext;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import java.util.Map;
import javax.ws.rs.Path;

@OpenAPIDefinition(info = @Info(title = "Cloudhopper Demo API",
        description = "",
        version = "1.0"
))
@Path("/hello")
public class HelloWorld implements CloudRequestHandler<Map<String,Object>, String> {

    @io.swagger.v3.oas.annotations.Operation(summary = "Say Hello",
            description = "Say Hello",
            operationId = "say-hello",
            extensions = {
                @io.swagger.v3.oas.annotations.extensions.Extension(
                        name = "x-amazon-apigateway-integration",
                        properties = {
                            @io.swagger.v3.oas.annotations.extensions.ExtensionProperty(name = "cloud-provider", value = "aws"),
                            @io.swagger.v3.oas.annotations.extensions.ExtensionProperty(name = "uri", value = "arn:aws:apigateway:eu-central-1:lambda:path/2015-03-31/functions/${HelloWorld_Arn}/invocations"),
                            @io.swagger.v3.oas.annotations.extensions.ExtensionProperty(name = "passthroughBehavior", value = "when_no_match"),
                            @io.swagger.v3.oas.annotations.extensions.ExtensionProperty(name = "httpMethod", value = "POST"),
                            @io.swagger.v3.oas.annotations.extensions.ExtensionProperty(name = "type", value = "aws_proxy"),
                            @io.swagger.v3.oas.annotations.extensions.ExtensionProperty(name = "provisonedConcurrencyEnabled", value = "true"),
                            @io.swagger.v3.oas.annotations.extensions.ExtensionProperty(name = "provisionedConcurrentExecutions", value = "2")}
                )
            }
    )
    @javax.ws.rs.GET
    @Override
    public String handleRequest(Map<String,Object> input, HandlerContext context) {
        return "hello";
    }

}
