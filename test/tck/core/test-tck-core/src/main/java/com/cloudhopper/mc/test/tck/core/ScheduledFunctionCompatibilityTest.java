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

import com.cloudhopper.mc.annotations.Schedule;
import com.cloudhopper.mc.test.tck.api.FeatureAwareTest;
import com.cloudhopper.mc.test.tck.api.RequiredFeature;
import com.cloudhopper.mc.test.tck.api.TestContext;
import java.util.List;
import org.junit.Assert;

public class ScheduledFunctionCompatibilityTest implements FeatureAwareTest {

    private static final String FUNCTION_NAME = "scheduled";

    @Override
    public void run(TestContext context) throws Exception {
        System.out.println("⏰ Waiting 70s to allow scheduled trigger to run...");
        Thread.sleep(70_000);

        List<String> logs = context.fetchLogs(FUNCTION_NAME);
        System.out.println("📜 Fetched logs:");
        logs.forEach(System.out::println);

        boolean triggered = logs.stream().anyMatch(line -> line.contains("Method was called"));
        Assert.assertTrue("Scheduled function did not appear to run as expected", triggered);
    }
    
    @Override
    public List<RequiredFeature> requiredFeatures() {
        return List.of(
                new RequiredFeature(Schedule.class.getName(), List.of(Schedule.ScheduleAttribute.CRON)));

    }
}
