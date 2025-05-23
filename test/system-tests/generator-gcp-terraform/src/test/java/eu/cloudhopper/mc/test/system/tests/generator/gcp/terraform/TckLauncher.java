package eu.cloudhopper.mc.test.system.tests.generator.gcp.terraform;

/*-
 * #%L
 * system-tests-generator-aws-terraform - a library from the "Cloudhopper" project.
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

import eu.cloudhopper.mc.generator.bindings.gcp.terraform.TestContextGcp;
import eu.cloudhopper.mc.test.tck.core.CompatibilityTestRunner;


/**
 * Entry point for the system test container for this generator.
 */
public class TckLauncher {
    public static void main(String[] args) throws Exception {
        CompatibilityTestRunner.runWith("gcp-terraform", new TestContextGcp());
    }
}
