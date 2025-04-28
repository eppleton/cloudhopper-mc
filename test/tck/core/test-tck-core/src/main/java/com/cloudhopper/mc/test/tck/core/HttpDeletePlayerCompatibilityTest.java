package com.cloudhopper.mc.test.tck.core;

/*-
 * #%L
 * test-tck-core - a library from the "Cloudhopper" project.
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
import com.cloudhopper.mc.test.support.CompatibilityTest;
import com.cloudhopper.mc.test.support.HttpClientHelper;
import com.cloudhopper.mc.test.support.TestContext;
import org.junit.Assert;

import java.net.URI;

public class HttpDeletePlayerCompatibilityTest implements CompatibilityTest {

    private static final String FUNCTION_NAME = "deletePlayer";

    @Override
    public void run(TestContext context) throws Exception {

        System.out.println("⏳ Waiting 30s for API Gateway to stabilize...");
        Thread.sleep(30000);

        String baseUrl = context.getHttpUrl(FUNCTION_NAME);
        URI url = URI.create(baseUrl.replace("{id}", "1"));

        System.out.println("🗑️ Sending DELETE to: " + url);
        String response = HttpClientHelper.delete(url);

        System.out.println("📩 Raw Response: " + response);

        Assert.assertTrue("Expected response to confirm deletion", response.contains("deleted"));

    }
}
