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
import com.cloudhopper.mc.test.tck.api.HttpClientHelper;
import com.cloudhopper.mc.test.tck.api.TestContext;
import com.cloudhopper.mc.test.domain.Match;
import com.cloudhopper.mc.test.tck.api.CompatibilityTest;
import com.cloudhopper.mc.test.tck.api.FeatureAwareTest;
import com.cloudhopper.mc.test.tck.api.RequiredFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;

import java.net.URI;
import java.util.List;

public class HttpGetMatchCompatibilityTest implements FeatureAwareTest {

    private static final String FUNCTION_NAME = "GetMatch";

    @Override
    public void run(TestContext context) throws Exception {

        String baseUrl = context.getHttpUrl(FUNCTION_NAME);
        URI url = URI.create(baseUrl.toString()
                .replace("{tournamentId}", "5")
                .replace("{matchId}", "42"));

        System.out.println("📞 Calling: " + url);

        HttpClientHelper.HttpResponse response = HttpClientHelper.get(url);

        System.out.println("📩 Raw Response: " + response.getBody());

        Assert.assertEquals(200, response.getStatusCode());

        Match match = new ObjectMapper().readValue(response.getBody(), Match.class);
        Assert.assertEquals(5, match.getTournamentId());
        Assert.assertEquals(42, match.getMatchId());
        Assert.assertEquals("PlayerA", match.getPlayer1());
        Assert.assertEquals("PlayerB", match.getPlayer2());
        Assert.assertEquals("Scheduled", match.getStatus());
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
