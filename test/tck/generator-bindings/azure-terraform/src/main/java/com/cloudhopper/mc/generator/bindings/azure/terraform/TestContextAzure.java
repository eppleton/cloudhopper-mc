package com.cloudhopper.mc.generator.bindings.azure.terraform;

/*-
 * #%L
 * generator-bindings-azure-terraform - a library from the "Cloudhopper" project.
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.*;

public class TestContextAzure implements TestContext {

    private final Path terraformDir = Path.of(System.getProperty("user.dir"), "target", "deployment", "azure");
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
        } catch (IOException | InterruptedException ex) {
            System.err.println("❗ Failed to resolve function URL: " + ex.getMessage());
        }
        return url;
    }

    @Override
    public Object invokeFunctionDirect(String functionUrl, Object input) {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        try {
            String payload = input != null
                    ? mapper.writeValueAsString(input)
                    : "{}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(functionUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String body = response.body();

            if (response.statusCode() >= 400) {
                System.err.println("❗ Azure Function returned error code " + response.statusCode());
                System.err.println("Response: " + body);
                throw new RuntimeException("Azure Function error: " + body);
            }

            JsonNode json = mapper.readTree(body);
            return mapper.treeToValue(json, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke Azure Function: " + functionUrl, e);
        }
    }

    @Override
    public List<String> fetchLogs(String functionName) {
        // Optional: implement with Azure Monitor or Log Analytics Workspace
        return List.of("[Azure log fetching not implemented]");
    }

    private String functionNameToOutputKey(String functionName) {
        return functionName.toLowerCase() + "_url";
    }
}