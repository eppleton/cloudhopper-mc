/*
 * Copyright (C) 2025 antonepple
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.cloudhopper.mc.provider.aws;

/*-
 * #%L
 * provider-aws - a library from the "Cloudhopper" project.
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

import com.amazonaws.services.lambda.runtime.Context;
import com.cloudhopper.mc.runtime.HandlerContext;

/**
 * Adapts AWS Lambda's {@link Context} to the {@link HandlerContext} interface expected by Cloudhopper.
 */
class AwsContextAdapter implements HandlerContext {

    private final Context awsContext;

    /**
     * Creates a new adapter for the given AWS Lambda context.
     *
     * @param awsContext the AWS context to adapt
     */
    public AwsContextAdapter(Context awsContext) {
        this.awsContext = awsContext;
    }

    /** {@inheritDoc} */
    @Override
    public String getRequestId() {
        return awsContext.getAwsRequestId();
    }

    /** {@inheritDoc} */
    @Override
    public String getFunctionName() {
        return awsContext.getFunctionName();
    }

    /** {@inheritDoc} */
    @Override
    public String getFunctionVersion() {
        return awsContext.getFunctionVersion();
    }

    /** {@inheritDoc} */
    @Override
    public String getInvokedFunctionArn() {
        return awsContext.getInvokedFunctionArn();
    }

    /** {@inheritDoc} */
    @Override
    public String getLogGroupName() {
        return awsContext.getLogGroupName();
    }

    /** {@inheritDoc} */
    @Override
    public String getLogStreamName() {
        return awsContext.getLogStreamName();
    }

    /** {@inheritDoc} */
    @Override
    public long getRemainingTimeInMillis() {
        return awsContext.getRemainingTimeInMillis();
    }

    /** {@inheritDoc} */
    @Override
    public int getMemoryLimitInMB() {
        return awsContext.getMemoryLimitInMB();
    }
    
}
