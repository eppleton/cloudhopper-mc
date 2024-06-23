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


import com.cloudhopper.mc.deployment.config.api.CloudRequestHandler;
import com.cloudhopper.mc.deployment.config.api.HandlerContext;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class GcpHttpRequestHandler<I, O> implements HttpFunction {

    private final CloudRequestHandler<I, O> handler;

    public GcpHttpRequestHandler(CloudRequestHandler<I, O> handler) {
        this.handler = handler;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder inputBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            inputBuilder.append(line);
        }

        I input = parseInput(inputBuilder.toString());
        O output = handler.handleRequest(input, new GcpContextAdapter(request));

        response.getWriter().write(output.toString());
    }

    private I parseInput(String input) {
        // Implement input parsing logic
        return null;
    }

    private static class GcpContextAdapter implements HandlerContext {
        private final HttpRequest httpRequest;

        public GcpContextAdapter(HttpRequest httpRequest) {
            this.httpRequest = httpRequest;
        }

        @Override
        public String getRequestId() {             
            return httpRequest.getFirstHeader("X-Request-ID").orElse(UUID.randomUUID().toString());
        }

        @Override
        public String getFunctionName() { return System.getenv("FUNCTION_NAME"); }

        @Override
        public String getFunctionVersion() { return System.getenv("FUNCTION_VERSION"); }

        @Override
        public String getInvokedFunctionArn() { return ""; }

        @Override
        public String getLogGroupName() { return ""; }

        @Override
        public String getLogStreamName() { return ""; }

        @Override
        public long getRemainingTimeInMillis() { return 0; }

        @Override
        public int getMemoryLimitInMB() { return Integer.parseInt(System.getenv("FUNCTION_MEMORY_MB")); }
    }
}
