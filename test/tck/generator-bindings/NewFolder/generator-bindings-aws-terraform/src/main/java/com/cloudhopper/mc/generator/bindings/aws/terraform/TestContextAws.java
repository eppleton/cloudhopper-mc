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
import com.cloudhopper.mc.test.tck.api.TestContext;
import java.io.IOException;
import software.amazon.awssdk.services.cloudwatchlogs.CloudWatchLogsClient;
import software.amazon.awssdk.services.cloudwatchlogs.model.*;
import java.nio.file.Path;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestContextAws implements TestContext {

    private final Path terraformDir = Path.of(System.getProperty("user.dir"), "target", "deployment", "aws");
    private final Set<String> usedFunctions = new HashSet<>();

    @Override
    public void deployTestFunctions() {
        try {
            System.out.println("🌍 CWD (user.dir): " + System.getProperty("user.dir"));
            System.out.println("📁 Terraform dir: " + terraformDir.toAbsolutePath());
            TerraformDeployer deployer = new TerraformDeployer(terraformDir);
            deployer.init();
            deployer.apply();
        } catch (Exception e) {
            throw new RuntimeException("Failed to deploy functions via Terraform", e);
        }
    }

    @Override
    public void cleanupTestFunctions() {
        try (CloudWatchLogsClient logsClient = CloudWatchLogsClient.create()) {
            // 🧹 First, clean up log groups for used functions
            for (String functionName : usedFunctions) {
                cleanupLogGroup(logsClient, functionName);
            }
        } catch (Exception e) {
            System.out.println("❗ Error during CloudWatch cleanup: " + e.getMessage());
        }

        // Then destroy infrastructure
        try {
            new TerraformDeployer(terraformDir).destroy();
        } catch (Exception e) {
            throw new RuntimeException("Failed to destroy deployed functions", e);
        }
    }

    @Override
    public String getHttpUrl(String functionId) {
        String url = null;
        usedFunctions.add(functionId);
        try {
            String key = functionNameToOutputKey(functionId);
            url = TerraformUtil.getOutputString(terraformDir, key);
            System.err.println("🌍 Resolved URL for function [" + functionId + "] key [" + key + "] → " + url);
        } catch (IOException ex) {
            Logger.getLogger(TestContextAws.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestContextAws.class.getName()).log(Level.SEVERE, null, ex);
        }
        return url;
    }

    @Override
    public Object invokeFunctionDirect(String functionName, Object input) {
        usedFunctions.add(functionName);
        throw new UnsupportedOperationException("Direct invocation not supported for AWS");
    }

    @Override
    public List<String> fetchLogs(String functionName) {
        usedFunctions.add(functionName);
        throw new UnsupportedOperationException("Fetching logs not yet supported for AWS");
    }

    private String functionNameToOutputKey(String functionName) {
        return functionName.toLowerCase()+ "_url";
    }

    private void cleanupLogGroup(CloudWatchLogsClient logsClient, String functionName) {
        String logGroupName = "/aws/lambda/" + functionName.toLowerCase();
        try {
            DeleteLogGroupRequest deleteRequest = DeleteLogGroupRequest.builder()
                    .logGroupName(logGroupName)
                    .build();
            logsClient.deleteLogGroup(deleteRequest);
            System.out.println("✅ Successfully deleted log group: " + logGroupName);
        } catch (ResourceNotFoundException e) {
            System.out.println("ℹ️ Log group already deleted: " + logGroupName);
        } catch (Exception e) {
            System.out.println("❗ Error deleting log group " + logGroupName + ": " + e.getMessage());
        }
    }
}
