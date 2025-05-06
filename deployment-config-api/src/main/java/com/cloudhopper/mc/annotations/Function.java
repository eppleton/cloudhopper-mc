package com.cloudhopper.mc.annotations;

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
 * Marks a class as a deployable cloud function.
 *
 * This annotation is used to indicate that the class should be discovered and
 * processed during code generation. It defines properties such as the
 * function's name and optional trigger configuration.
 *
 * Example usage:
 * <pre>
 * {@code
 * @Function(name = "hello-world")
 * public class HelloFunction implements CloudRequestHandler<String, String> {
 *     public String handleRequest(String input, HandlerContext context) {
 *         return "Hello, " + input;
 *     }
 * }
 * }
 * </pre>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Function {

    /**
     * Constants representing the attribute names of {@link Function}.
     *
     * These are intended for use by generator implementors when declaring which
     * attributes their generator supports in {see: com.cloudhopper.mc.generator.api.annotations.GeneratorFeature}.
     *
     * Example usage:
     * <pre>{@code
     * @GeneratorFeature(
     *     supportedAnnotation = Function.class,
     *     supportedAttributes = {
     *         FunctionAttribute.NAME,
     *         FunctionAttribute.MEMORY
     *     }
     * )
     * }</pre>
     *
     * This class is not relevant for application developers and may be ignored
     * in function code.
     */
    public static class FunctionAttribute {

        public static final String NAME = "name";
        public static final String MEMORY = "memory";
        public static final String TIMEOUT = "timeout";
        public static final String MIN_INSTANCES = "minInstances";
        public static final String ARCHITECTURE = "architecture";
    }

    /**
     * The unique name of the function in the deployment target.
     *
     * @return the function name
     */
    String name();

    /**
     * The maximum memory allocated to the function (in megabytes). Default is
     * 128 MB.
     *
     * @return the memory limit in MB
     */
    int memory() default 128;

    /**
     * The function timeout in seconds. Defines how long the function may run
     * before being terminated. Default is 30 seconds.
     *
     * @return timeout in seconds
     */
    int timeout() default 30;

    /**
     * The minimum number of instances to keep warm. This enables pre-warming
     * for reduced cold starts on some platforms. Default is 0.
     *
     * @return minimum instance count
     */
    int minInstances() default 0;

    /**
     * The function architecture. Default is arm64.
     *
     * @return function architecture
     */
    String architecture() default "arm64";
}
