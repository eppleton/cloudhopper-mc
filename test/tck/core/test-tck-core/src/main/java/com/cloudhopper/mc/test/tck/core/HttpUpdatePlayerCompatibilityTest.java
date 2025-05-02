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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cloudhopper.mc.test.domain.Player;
import com.cloudhopper.mc.test.support.CompatibilityTest;
import com.cloudhopper.mc.test.support.TestContext;
import org.junit.Assert;

import java.net.URI;

public class HttpUpdatePlayerCompatibilityTest implements CompatibilityTest {

    private static final String FUNCTION_NAME = "updatePlayer";

    @Override
    public void run(TestContext context) throws Exception {
        String baseUrl = context.getHttpUrl(FUNCTION_NAME);
        URI url = new URI(baseUrl.replace("{id}", "1")); // Replace {id} manually

        Player updatedPlayer = new Player(1, "UpdatedName", 15);
        String jsonBody = new ObjectMapper().writeValueAsString(updatedPlayer);

        System.out.println("📦 Sending updated player to: " + url);
        HttpClientHelper.HttpResponse response = HttpClientHelper.put(url, jsonBody);

        System.out.println("📩 Raw Response: " + response.getBody());

        Player player = new ObjectMapper().readValue(response.getBody(), Player.class);
        System.out.println("👨 Parsed Player: " + player.getName() + " (ID " + player.getId() + ", Ranking " + player.getRanking() + ")");

        Assert.assertEquals("Player ID mismatch", 1, player.getId());
        Assert.assertEquals("Player name mismatch", "UpdatedName", player.getName());
        Assert.assertEquals("Player ranking mismatch", 15, player.getRanking());
    }
}
