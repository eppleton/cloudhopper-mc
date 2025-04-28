package com.cloudhopper.mc.test.functions;

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
import com.cloudhopper.mc.annotations.ApiOperation;
import com.cloudhopper.mc.annotations.Function;
import com.cloudhopper.mc.runtime.CloudRequestHandler;
import com.cloudhopper.mc.runtime.HandlerContext;
import com.cloudhopper.mc.test.domain.Player;

import java.util.Map;

public class GetPlayerFunction implements CloudRequestHandler<Void, Player> {

    @ApiOperation(
            summary = "Fetch player by ID",
            description = "Returns a player from the ping pong tournament database by ID.",
            operationId = "getPlayer",
            path = "/player/{id}",
            method = "GET"
    )
    @Function(name = "getPlayer")
    @Override
    public Player handleRequest(Void input, Map<String, String> pathParams, Map<String, String> queryParams, HandlerContext context) {
        String idStr = pathParams.get("id");
        int id = idStr != null ? Integer.parseInt(idStr) : 1;

        // For now, just return a dummy player
        return new Player(id, "Player" + id, 42);
    }
}
