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
import com.cloudhopper.mc.generator.api.GeneratorFeatureInfo;
import com.cloudhopper.mc.test.support.CompatibilityTest;
import com.cloudhopper.mc.test.support.TestContext;

import java.util.ArrayList;
import java.util.List;

/**
 * The TCK test runner for verifying generator compatibility.
 */
public class CompatibilityTestRunner {

    public static void runWith(String generatorId, TestContext context) throws Exception {
        System.out.println("🔍 Running TCK for generator: " + generatorId);

        GeneratorFeatureInfo info = GeneratorFeatureInfo.Loader.loadFeaturesFor(generatorId);
        if (info == null) {
            throw new IllegalStateException("Could not load generator features for: " + generatorId);
        }

        List<CompatibilityTest> testsToRun = new ArrayList<>();
        if (supportsAnnotation(info, "com.cloudhopper.mc.annotations.ApiOperation")) {
            // minimal tests to ensure an API can be called
            testsToRun.add(new HttpFunctionCompatibilityTest());
            // test if "complex" objects can be used
            testsToRun.add(new HttpGetPlayerCompatibilityTest());
            // test post
            testsToRun.add(new HttpRegisterPlayerCompatibilityTest());
            // test put
            testsToRun.add(new HttpUpdatePlayerCompatibilityTest());
            // test delete
            testsToRun.add(new HttpDeletePlayerCompatibilityTest());
        }
        try {
            System.out.println("🚀 Deploying test functions...");
            context.deployTestFunctions();
            System.out.println("⏳ Waiting 20s for API Gateway to stabilize...");
            Thread.sleep(20000);

            for (CompatibilityTest test : testsToRun) {
                String name = test.getClass().getSimpleName();
                System.out.println("\n▶ Running test: " + name);
                try {
                    test.run(context);
                    System.out.println("✅ " + name + " PASSED");
                } catch (Throwable t) {
                    System.out.println("❌ " + name + " FAILED");
                    t.printStackTrace();
                }
            }
        } finally {
            System.out.println("\n🧹 Cleaning up deployed functions...");
            context.cleanupTestFunctions();
        }
    }

    private static boolean supportsAnnotation(GeneratorFeatureInfo info, String annotationFqcn) {
        return info.getFeatures().stream()
                .anyMatch(f -> annotationFqcn.equals(f.getSupportedAnnotation()));
    }
}
