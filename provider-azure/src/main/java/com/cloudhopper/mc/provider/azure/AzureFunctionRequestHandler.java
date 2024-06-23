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


import com.cloudhopper.mc.deployment.config.api.CloudRequestHandler;
import com.cloudhopper.mc.deployment.config.api.HandlerContext;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;
import java.util.Optional;

public class AzureFunctionRequestHandler<I, O> {

    private final CloudRequestHandler<I, O> handler;

    public AzureFunctionRequestHandler(CloudRequestHandler<I, O> handler) {
        this.handler = handler;
    }

    @FunctionName("handleRequest")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            ExecutionContext context) {
        String input = request.getBody().orElse("");
        I parsedInput = parseInput(input);
        O output = handler.handleRequest(parsedInput, new AzureContextAdapter(context));

        return request.createResponseBuilder(HttpStatus.OK).body(output.toString()).build();
    }

    private I parseInput(String input) {
        // Implement input parsing logic
        return null;
    }

    private static class AzureContextAdapter implements HandlerContext {
        private final ExecutionContext executionContext;

        public AzureContextAdapter(ExecutionContext executionContext) {
            this.executionContext = executionContext;
        }

        @Override
        public String getRequestId() { return executionContext.getInvocationId(); }

        @Override
        public String getFunctionName() { return executionContext.getFunctionName(); }

        @Override
        public String getFunctionVersion() { return ""; }

        @Override
        public String getInvokedFunctionArn() { return ""; }

        @Override
        public String getLogGroupName() { return ""; }

        @Override
        public String getLogStreamName() { return ""; }

        @Override
        public long getRemainingTimeInMillis() { return 0; }

        @Override
        public int getMemoryLimitInMB() { return 0; }
    }
}
