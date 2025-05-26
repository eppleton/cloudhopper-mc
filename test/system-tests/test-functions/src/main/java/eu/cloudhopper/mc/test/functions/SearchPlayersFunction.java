package eu.cloudhopper.mc.test.functions;

/*-
 * #%L
 * test-functions - a library from the "Cloudhopper" project.
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


import eu.cloudhopper.mc.annotations.Function;
import eu.cloudhopper.mc.test.domain.Player;

import java.util.ArrayList;
import java.util.List;
import eu.cloudhopper.mc.annotations.HttpTrigger;
import eu.cloudhopper.mc.annotations.QueryParam;

public class SearchPlayersFunction  {

    @HttpTrigger(
            summary = "Search players by ranking",
            description = "Returns a list of players with ranking between minRanking and maxRanking.",
            operationId = "searchPlayers",
            path = "/players/search",
            method = "GET"
    )
    @Function(name = "searchPlayers")
    public List<Player> searchPlayers(@QueryParam Integer minRanking, @QueryParam Integer maxRanking) {

        List<Player> players = new ArrayList<>();
        for (int i = minRanking; i <= maxRanking; i += 10) {
            players.add(new Player(i, "Player" + i, i));
        }
        return players;
    }
}
