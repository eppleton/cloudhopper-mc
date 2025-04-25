package com.cloudhopper.mc.generator.bindings.aws.terraform;

/*-
 * #%L
 * generator-bindings-aws-terraform - a library from the "Cloudhopper" project.
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

import com.cloudhopper.mc.test.support.TerraformDeployer;
import com.cloudhopper.mc.test.support.TerraformUtil;
import com.cloudhopper.mc.test.support.TestContext;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;

public class TestContextAws implements TestContext {

    private final Path terraformDir = Path.of("target/deployment/aws");

    @Override
    public void deployTestFunctions() {
        try {
            TerraformDeployer deployer = new TerraformDeployer(terraformDir);
            deployer.init();
            deployer.apply();
        } catch (Exception e) {
            throw new RuntimeException("Failed to deploy functions via Terraform", e);
        }
    }

    @Override
    public void cleanupTestFunctions() {
        try {
            new TerraformDeployer(terraformDir).destroy();
        } catch (Exception e) {
            throw new RuntimeException("Failed to destroy deployed functions", e);
        }
    }

    @Override
    public URI getHttpUrl(String functionName) {
        try {
            String key = functionNameToOutputKey(functionName);
            String url = TerraformUtil.getOutputString(terraformDir, key);
            return URI.create(url);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch HTTP URL from Terraform output", e);
        }
    }

    @Override
    public Object invokeFunctionDirect(String functionName, Object input) {
        throw new UnsupportedOperationException("Direct invocation not supported for AWS");
    }

    @Override
    public List<String> fetchLogs(String functionName) {
        throw new UnsupportedOperationException("Fetching logs not yet supported for AWS");
    }

    private String functionNameToOutputKey(String functionName) {
        return functionName.toLowerCase().replace("function", "") + "_url";
    }
}
