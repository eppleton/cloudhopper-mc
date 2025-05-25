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

import eu.cloudhopper.mc.annotations.HttpTrigger;
import eu.cloudhopper.mc.annotations.PathParam;

public class GetPlayerFunction  {

    @HttpTrigger(
            summary = "Fetch player by ID",
            description = "Returns a player from the ping pong tournament database by ID.",
            operationId = "getPlayer",
            path = "/player/{id}",
            method = "GET"
    )
    @Function(name = "getPlayer")
    public Player getPlayer(@PathParam Integer id) {

        // For now, just return a dummy player
        return new Player(id, "Player" + id, 42);
    }
}
