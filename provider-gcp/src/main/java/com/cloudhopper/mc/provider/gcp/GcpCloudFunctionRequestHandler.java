package com.cloudhopper.mc.provider.gcp;

/*-
 * #%L
 * provider-gcp - a library from the "Cloudhopper" project.
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

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.cloudhopper.mc.deployment.config.api.CloudRequestHandler;
import com.cloudhopper.mc.deployment.config.api.HandlerContext;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public abstract class GcpCloudFunctionRequestHandler<I, O> implements HttpFunction {

    private final CloudRequestHandler<I, O> handler;
    private final Type inputType;
    private final Gson gson = new Gson();

    protected GcpCloudFunctionRequestHandler(CloudRequestHandler<I, O> handler, TypeToken<I> typeToken) {
        this.handler = handler;
        this.inputType = typeToken.getType();
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        I input = parseInput(request);
        O output = handler.handleRequest(input, new GcpContextAdapter(request));
        writeOutput(response, output);
    }

    private I parseInput(HttpRequest request) throws IOException {
        String body = request.getReader().lines().reduce("", (accumulator, actual) -> accumulator + actual);
        return gson.fromJson(body, inputType);
    }

    private void writeOutput(HttpResponse response, O output) throws IOException {
        response.setContentType("application/json");
        BufferedWriter writer = response.getWriter();
        writer.write(gson.toJson(output));
    }


    private static class GcpContextAdapter implements HandlerContext {
        private final HttpRequest request;

        public GcpContextAdapter(HttpRequest request) {
            this.request = request;
        }

        @Override
        public String getRequestId() {
            return request.getHeaders().get("X-Cloud-Trace-Context").get(0);
        }

        @Override
        public String getFunctionName() {
            return System.getenv("K_SERVICE");
        }

        @Override
        public String getFunctionVersion() {
            return System.getenv("K_REVISION");
        }

        @Override
        public String getInvokedFunctionArn() {
            return null; // Not applicable for GCP
        }

        @Override
        public String getLogGroupName() {
            return null; // Not directly available in GCP
        }

        @Override
        public String getLogStreamName() {
            return null; // Not directly available in GCP
        }

        @Override
        public long getRemainingTimeInMillis() {
            return -1; // Not directly available in GCP
        }

        @Override
        public int getMemoryLimitInMB() {
            return Integer.parseInt(System.getenv("FUNCTION_MEMORY_MB"));
        }
    }
}