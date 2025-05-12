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
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;

public class TestContextAzure implements TestContext {

    private final Path terraformDir = Path.of(System.getProperty("user.dir"), "target", "deployment", "azure-terraform-java21");
    private final Set<String> usedFunctions = new HashSet<>();
    private final String azureFunctionBaseUrl;

    public TestContextAzure(String azureFunctionBaseUrl) {
        this.azureFunctionBaseUrl = azureFunctionBaseUrl;  // e.g., "https://my-shared-function-app.azurewebsites.net/api/"
    }

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

    public Object invokeFunctionDirect(String functionName, Object input) {
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        try {
            String fullUrl = azureFunctionBaseUrl.endsWith("/")
                    ? azureFunctionBaseUrl + functionName
                    : azureFunctionBaseUrl + "/" + functionName + "_http_function_trigger";
            System.err.println("full URL " + fullUrl);
            HttpRequest request;

            if (input == null) {
                request = HttpRequest.newBuilder()
                        .uri(URI.create(fullUrl))
                        .GET()
                        .build();
            } else {
                String payload = mapper.writeValueAsString(input);
                request = HttpRequest.newBuilder()
                        .uri(URI.create(fullUrl))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(payload))
                        .build();
            }

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String body = response.body();
            if (response.statusCode() >= 400) {
                System.err.println("❗ Azure Function returned error code " + response.statusCode());
                System.err.println("Response: " + body);
                throw new RuntimeException("Azure Function error: " + body);
            }

            JsonNode root = mapper.readTree(body);
            if (root.isObject() && root.has("result")) {
                return mapper.treeToValue(root.get("result"), Object.class);
            } else if (root.isValueNode()) {
                return mapper.treeToValue(root, Object.class);
            } else {
                return root;
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke Azure Function: " + functionName, e);
        }
    }

    @Override
    public List<String> fetchLogs(String functionName) {
        try {
            String subscriptionId = System.getenv("AZURE_SUBSCRIPTION_ID");
            String resourceGroup = System.getenv("AZURE_RESOURCE_GROUP");
            String appInsightsName = System.getenv("AZURE_APPINSIGHTS_NAME");
            String accessToken = getAzureAccessToken(); // OAuth2 token from CLI or managed identity

            String query = String.format(
                    "traces | where timestamp > ago(5m) | where message contains '%s' | project message",
                    functionName
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format(
                            "https://management.azure.com/subscriptions/%s/resourceGroups/%s/providers/microsoft.insights/components/%s/query?api-version=2018-04-20",
                            subscriptionId, resourceGroup, appInsightsName)))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"query\": \"" + query + "\"}"))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            JsonNode tables = root.path("tables");

            List<String> logs = new ArrayList<>();
            if (tables.isArray() && tables.size() > 0) {
                JsonNode rows = tables.get(0).path("rows");
                for (JsonNode row : rows) {
                    logs.add(row.get(0).asText());
                }
            }
            return logs;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Azure Function logs", e);
        }
    }

    public static String getAzureAccessToken() {
        try {
            String tenantId = System.getenv("ARM_TENANT_ID");
            String clientId = System.getenv("ARM_CLIENT_ID");
            String clientSecret = System.getenv("ARM_CLIENT_SECRET");

            if (tenantId == null || clientId == null || clientSecret == null) {
                throw new IllegalStateException("Azure credentials are not set (ARM_TENANT_ID, ARM_CLIENT_ID, ARM_CLIENT_SECRET)");
            }

            String body = "grant_type=client_credentials"
                    + "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                    + "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8)
                    + "&scope=https%3A%2F%2Fmanagement.azure.com%2F.default";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new RuntimeException("Failed to fetch Azure access token: " + response.body());
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            return root.get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to obtain Azure access token", e);
        }
    }

    private String functionNameToOutputKey(String functionName) {
        return functionName.toLowerCase() + "_url";
    }
}
