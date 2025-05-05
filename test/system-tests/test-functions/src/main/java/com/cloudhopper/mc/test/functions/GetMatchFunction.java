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
import com.cloudhopper.mc.test.domain.Match;

import java.util.Map;

public class GetMatchFunction implements CloudRequestHandler<Void, Match> {

    @ApiOperation(
            summary = "Fetch a match by tournament and match ID",
            description = "Returns a match in a tournament based on provided tournamentId and matchId.",
            operationId = "getMatch",
            path = "/tournament/{tournamentId}/match/{matchId}",
            method = "GET"
    )
    @Function(name = "getMatch")
    @Override
    public Match handleRequest(Void input, Map<String, String> pathParams, Map<String, String> queryParams, HandlerContext context) {
        String tournamentIdStr = pathParams.get("tournamentId");
        String matchIdStr = pathParams.get("matchId");

        int tournamentId = tournamentIdStr != null ? Integer.parseInt(tournamentIdStr) : 0;
        int matchId = matchIdStr != null ? Integer.parseInt(matchIdStr) : 0;

        return new Match(tournamentId, matchId, "PlayerA", "PlayerB", "Scheduled");
    }
}
