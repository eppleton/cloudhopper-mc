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
import com.cloudhopper.mc.test.support.HttpClientHelper;
import com.cloudhopper.mc.test.support.TestContext;
import com.cloudhopper.mc.test.support.CompatibilityTest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import org.junit.Assert;

/**
 * Compatibility test for HTTP-exposed functions. Requires a function with
 *
 * @ApiOperation(path = "/ping") that returns "pong".
 */
public class HttpFunctionCompatibilityTest implements CompatibilityTest {

    private static final String FUNCTION_NAME = "Ping";

    @Override
    public void run(TestContext context) throws Exception {
        String url = context.getHttpUrl(FUNCTION_NAME);
        System.out.println("📞 Calling: " + url);

        HttpClientHelper.HttpResponse response = HttpClientHelper.get(URI.create(url));
        System.out.println("📩 Raw Response: " + response.getBody());

        ObjectMapper mapper = new ObjectMapper();
        String unwrappedResponse = mapper.readValue(response.getBody(), String.class);

        System.out.println("🏓 Response: " + unwrappedResponse);
        Assert.assertEquals("Expected response to be 'pong'", "pong", unwrappedResponse);
    }
}
