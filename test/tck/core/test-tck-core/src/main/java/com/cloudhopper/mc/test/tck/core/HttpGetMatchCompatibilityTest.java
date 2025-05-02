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
import com.cloudhopper.mc.test.domain.Match;
import com.cloudhopper.mc.test.support.CompatibilityTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;

import java.net.URI;

public class HttpGetMatchCompatibilityTest implements CompatibilityTest {

    private static final String FUNCTION_NAME = "GetMatch";

    @Override
    public void run(TestContext context) throws Exception {
        System.out.println("⏳ Waiting 30s for API Gateway to stabilize...");
        Thread.sleep(30000);

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
}
