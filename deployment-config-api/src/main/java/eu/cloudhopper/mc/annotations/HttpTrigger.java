package eu.cloudhopper.mc.annotations;

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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides metadata about an HTTP-accessible function for API gateway integration.
 * <p>
 * This annotation defines API-related properties such as the HTTP path, method,
 * operation ID, summary, description, and parameters. It is used by generators
 * to configure platform-specific API gateway resources or routing rules.
 * <p>
 * Each generator may use this metadata differently depending on platform capabilities
 * (e.g., API Gateway routing in AWS, or HTTP trigger configuration in GCP).
 * <h2>Example</h2>
 * <pre>{@code
 @Function(name = "hello")
 @HttpTrigger(
     method = "GET",
     path = "/hello/{name}",
     summary = "Says hello",
     description = "Returns a greeting using the given name",
     operationId = "sayHello",
 )
 public class HelloFunction implements CloudRequestHandler<Void, String> {
     public String handleRequest(Void input, HandlerContext context) {
         return "Hello!";
     }
 }
 }</pre>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface HttpTrigger {

    /**
     * A short summary of the operation.
     * This may be used for documentation or API descriptions.
     *
     * @return summary string
     */
    String summary() default "";

    /**
     * A longer, detailed description of the operation.
     * Useful for documentation or OpenAPI generation.
     *
     * @return description string
     */
    String description() default "";

    /**
     * A unique identifier for the operation (e.g. "sayHello").
     * This may be used for client code generation or internal mapping.
     *
     * @return operation ID
     */
    String operationId() default "";

    /**
     * The relative path for the function’s HTTP endpoint (e.g. "/hello/{name}").
     *
     * @return endpoint path
     */
    String path() default "";

    /**
     * The HTTP method for the endpoint (e.g. "GET", "POST").
     *
     * @return HTTP method
     */
    String method() default "";


    /**
     * Constants representing attribute names of this annotation.
     * <p>
     * Intended for use by generator implementors to declare support for individual attributes
     * in {see: eu.cloudhopper.mc.generator.api.annotations.GeneratorFeature}.
     */
    class HttpTriggerAttribute {
        public static final String SUMMARY = "summary";
        public static final String DESCRIPTION = "description";
        public static final String OPERATION_ID = "operationId";
        public static final String PATH = "path";
        public static final String METHOD = "method";
    }
}