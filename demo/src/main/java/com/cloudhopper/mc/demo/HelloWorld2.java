package com.cloudhopper.mc.demo;

/*-
 * #%L
 * demo - a library from the "Cloudhopper" project.
 * 
 * Eppleton IT Consulting designates this particular file as subject to the "Classpath"
 * exception as provided in the README.md file that accompanies this code.
 * %%
 * Copyright (C) 2024 Eppleton IT Consulting
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

import com.cloudhopper.mc.Function;
import com.cloudhopper.mc.deployment.config.api.CloudRequestHandler;
import com.cloudhopper.mc.deployment.config.api.HandlerContext;


public class HelloWorld2 implements CloudRequestHandler<String, String>{
    
    
    @Function(  name = "hello_world_2")
    @Override
    public String handleRequest(String input, HandlerContext context) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    
    
}
