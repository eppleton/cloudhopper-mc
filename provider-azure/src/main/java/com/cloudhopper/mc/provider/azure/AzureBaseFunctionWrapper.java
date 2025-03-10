package com.cloudhopper.mc.provider.azure;

/*-
 * #%L
 * provider-azure - a library from the "Cloudhopper" project.
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


import com.microsoft.azure.functions.*;
import com.cloudhopper.mc.deployment.config.api.CloudRequestHandler;
import com.cloudhopper.mc.deployment.config.api.HandlerContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.IOException;
import java.lang.reflect.Type;

public abstract class AzureBaseFunctionWrapper<I, O> {
    protected final CloudRequestHandler<I, O> handler;
    protected final Type inputType;
    protected final ObjectMapper objectMapper = new ObjectMapper();
    private final Gson gson = new Gson();

    protected AzureBaseFunctionWrapper(CloudRequestHandler<I, O> handler, Type inputType) {
        this.handler = handler;
        this.inputType = inputType;
    }

    protected HttpResponseMessage handleRequest(HttpRequestMessage<String> request, ExecutionContext context) {
        try {
            I input = parseInput(request);
            O output = handler.handleRequest(input, new AzureContextAdapter(context));
            return createSuccessResponse(request, output);
        } catch (Exception e) {
            return createErrorResponse(request, e);
        }
    }

    private I parseInput(HttpRequestMessage<String> request) throws IOException {
        return gson.fromJson(request.getBody(), inputType);
    }

    private HttpResponseMessage createSuccessResponse(HttpRequestMessage<String> request, O output) throws JsonProcessingException {
        return request.createResponseBuilder(HttpStatus.OK)
                .body(objectMapper.writeValueAsString(output))
                .header("Content-Type", "application/json")
                .build();
    }

    private HttpResponseMessage createErrorResponse(HttpRequestMessage<String> request, Exception e) {
        return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error processing request: " + e.getMessage())
                .build();
    }

    private static class AzureContextAdapter implements HandlerContext {
        private final ExecutionContext context;

        public AzureContextAdapter(ExecutionContext context) {
            this.context = context;
        }

        @Override
        public String getRequestId() {
            return context.getInvocationId();
        }

        @Override
        public String getFunctionName() {
            return context.getFunctionName();
        }

        @Override
        public String getFunctionVersion() {
            return System.getenv("FUNCTIONS_EXTENSION_VERSION");
        }

        @Override
        public String getInvokedFunctionArn() {
            return null; // Not applicable for Azure
        }

        @Override
        public String getLogGroupName() {
            return null; // Not directly available in Azure
        }

        @Override
        public String getLogStreamName() {
            return null; // Not directly available in Azure
        }

        @Override
        public long getRemainingTimeInMillis() {
            return -1; // Not directly available in Azure
        }

        @Override
        public int getMemoryLimitInMB() {
            return Integer.parseInt(System.getenv("WEBSITE_MEMORY_LIMIT_MB"));
        }
    }
}