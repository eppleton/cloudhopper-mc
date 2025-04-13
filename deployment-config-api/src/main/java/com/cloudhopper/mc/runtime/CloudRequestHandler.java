package com.cloudhopper.mc.runtime;

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

/**
 * A user-defined function handler for processing cloud function requests.
 * <p>
 * Each function must implement this interface to define how input is handled
 * and output is returned. The runtime platform is responsible for invoking
 * {@link #handleRequest(Object, HandlerContext)} with the appropriate input and context.
 *
 * @param <I> The input type (e.g., deserialized JSON request body)
 * @param <O> The output type (e.g., response body)
 */
public interface CloudRequestHandler<I, O> {

    /**
     * Handles an incoming request.
     *
     * @param input   the deserialized input object
     * @param context runtime context information about the invocation
     * @return a response object
     */
    O handleRequest(I input, HandlerContext context);
}