package com.cloudhopper.mc.test.support;

/*-
 * #%L
 * test-support - a library from the "Cloudhopper" project.
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
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TerraformDeployer {

    private final Path workingDir;

    public TerraformDeployer(Path workingDir) {
        this.workingDir = workingDir;
    }

    public void init() throws IOException, InterruptedException {
        System.out.println("🚧 Running terraform in: " + workingDir.toAbsolutePath());

        if (!workingDir.toFile().exists()) {
            throw new IllegalStateException("❌ Terraform directory not found: " + workingDir.toAbsolutePath());
        }
        run("init");
    }

    public void apply() throws IOException, InterruptedException {
        run("apply", "-auto-approve");
    }

    public void destroy() throws IOException, InterruptedException {
        run("destroy", "-auto-approve");
    }

    private void run(String... args) throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();
        command.add("terraform");
        command.addAll(Arrays.asList(args));
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(workingDir.toFile());
        pb.inheritIO();
        pb.redirectOutput(new File(args[0]+"-output.log"));
        pb.redirectErrorStream(true); // redirects stderr to stdout
        System.out.println(command);
        Map<String, String> env = pb.environment();
        env.put("PATH", System.getenv("PATH"));

        Process p = pb.start();
        if (p.waitFor() != 0) {
            throw new RuntimeException("Terraform failed: " + Arrays.toString(args));
        }
    }
}
