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
import com.cloudhopper.mc.annotations.ApiOperation;
import com.cloudhopper.mc.annotations.Function;
import com.cloudhopper.mc.test.domain.Player;
import com.cloudhopper.mc.test.tck.api.CompatibilityTest;
import com.cloudhopper.mc.test.tck.api.FeatureAwareTest;
import com.cloudhopper.mc.test.tck.api.HttpClientHelper;
import com.cloudhopper.mc.test.tck.api.RequiredFeature;
import com.cloudhopper.mc.test.tck.api.TestContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.List;
import org.junit.Assert;

public class HttpRegisterPlayerCompatibilityTest implements FeatureAwareTest {

    private static final String FUNCTION_NAME = "registerPlayer";

    @Override
    public void run(TestContext context) throws Exception {
        String baseUrl = context.getHttpUrl(FUNCTION_NAME);
        URI uri = URI.create(baseUrl); // Replace path variable manually

        System.out.println("📞 Calling: " + uri);
        Player newPlayer = new Player(0, "Toni", 5);
        String jsonBody = new ObjectMapper().writeValueAsString(newPlayer);

        HttpClientHelper.HttpResponse response = HttpClientHelper.post(uri, jsonBody);
        System.out.println("📩 Raw Response: " + response.getBody());

        ObjectMapper mapper = new ObjectMapper();
        Player player = mapper.readValue(response.getBody(), Player.class);

        System.out.println("👨 Parsed Player: " + player.getName() + " (ID " + player.getId() + ", Ranking " + player.getRanking() + ")");

        Assert.assertEquals(123, player.getId());
        Assert.assertEquals("Toni", player.getName());
        Assert.assertEquals(5, player.getRanking());
    }

    @Override
    public List<RequiredFeature> requiredFeatures() {
        return List.of(
                new RequiredFeature(Function.class.getName(), List.of(Function.FunctionAttribute.NAME)),
                new RequiredFeature(ApiOperation.class.getName(),
                        List.of(
                                ApiOperation.ApiOperationAttribute.METHOD,
                                ApiOperation.ApiOperationAttribute.OPERATION_ID,
                                ApiOperation.ApiOperationAttribute.PATH
                        )
                ));
    }
}
