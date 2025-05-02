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
import com.cloudhopper.mc.test.domain.Player;
import com.cloudhopper.mc.test.support.CompatibilityTest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;

import java.net.URI;
import java.util.List;

public class HttpSearchPlayersCompatibilityTest implements CompatibilityTest {

    private static final String FUNCTION_NAME = "SearchPlayers";

    @Override
    public void run(TestContext context) throws Exception {
        URI baseUrl = new URI(context.getHttpUrl(FUNCTION_NAME));
        // Add query params manually
        URI url = new URI(baseUrl.toString() + "?minRanking=10&maxRanking=20");

        System.out.println("📞 Calling: " + url);

        HttpClientHelper.HttpResponse response = HttpClientHelper.get(url);

        System.out.println("📩 Raw Response: " + response.getBody());

        Assert.assertEquals(200, response.getStatusCode());

        ObjectMapper mapper = new ObjectMapper();
        List<Player> players = mapper.readValue(response.getBody(), new TypeReference<List<Player>>() {});

        Assert.assertFalse("Expected non-empty player list", players.isEmpty());

        for (Player player : players) {
            System.out.println("🏓 Player: " + player.getName() + " (Ranking: " + player.getRanking() + ")");
            Assert.assertTrue(player.getRanking() >= 10 && player.getRanking() <= 20);
        }
    }
}
