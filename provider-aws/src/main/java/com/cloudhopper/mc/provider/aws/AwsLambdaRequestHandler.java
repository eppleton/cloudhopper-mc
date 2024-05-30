package com.cloudhopper.mc.provider.aws;

/*-
 * #%L
 * provider-aws - a library from the "Cloudhopper" project.
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

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.cloudhopper.mc.deployment.config.api.CloudRequestHandler;
import com.cloudhopper.mc.deployment.config.api.HandlerContext;

public abstract class AwsLambdaRequestHandler<I, O> implements RequestHandler<I, O> {

    private final CloudRequestHandler<I, O> handler;

    protected AwsLambdaRequestHandler(CloudRequestHandler<I, O> handler) {
        this.handler = handler;
    }

    @Override
    public O handleRequest(I input, Context awsContext) {
        return handler.handleRequest(input, new AwsContextAdapter(awsContext));
    }

    private static class AwsContextAdapter implements HandlerContext {
        private final Context awsContext;

        public AwsContextAdapter(Context awsContext) {
            this.awsContext = awsContext;
        }

        @Override
        public String getRequestId() {
            return awsContext.getAwsRequestId();
        }

        @Override
        public String getFunctionName() {
            return awsContext.getFunctionName();
        }

        @Override
        public String getFunctionVersion() {
            return awsContext.getFunctionVersion();
        }

        @Override
        public String getInvokedFunctionArn() {
            return awsContext.getInvokedFunctionArn();
        }

        @Override
        public String getLogGroupName() {
            return awsContext.getLogGroupName();
        }

        @Override
        public String getLogStreamName() {
            return awsContext.getLogStreamName();
        }

        @Override
        public long getRemainingTimeInMillis() {
            return awsContext.getRemainingTimeInMillis();
        }

        @Override
        public int getMemoryLimitInMB() {
            return awsContext.getMemoryLimitInMB();
        }
    }
}
