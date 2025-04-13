/*
 * Copyright (C) 2025 antonepple
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.cloudhopper.mc.generator.generic.annotations;

/*-
 * #%L
 * deployment-config-generator-generic - a library from the "Cloudhopper" project.
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

import com.cloudhopper.mc.generator.api.GenerationPhase;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes a single template to be rendered by the
 * {@link com.cloudhopper.mc.generator.generic.GenericDeploymentConfigGenerator}.
 * <p>
 * The output location, file type, and rendering phase are all controlled
 * via this annotation.
 */
@Retention(value = RetentionPolicy.SOURCE)
@Target(value = {})
public @interface Template {

    /**
     * The generation phase in which this template should be executed.
     *
     * @return the generation phase
     */
    GenerationPhase phase();

    /**
     * The name of the template file (without the file extension).
     * <p>
     * For example, if you provide "function", the system will look for
     * {@code function.ftl} in the generator's template folder.
     *
     * @return the template base name
     */
    String templateName();

    /**
     * A short description of the template's purpose.
     *
     * @return the template description
     */
    String description() default "";

    /**
     * The file extension to apply to the rendered output (e.g., "tf",
     * "json", "java").
     *
     * @return the output file extension
     */
    String outputFileExtension();

    /**
     * The relative subdirectory where this file should be written (e.g.,
     * "deployment/aws"). If empty, the file is written to the root output
     * directory.
     *
     * @return the output subdirectory
     */
    String outputSubDirectory() default "";

    /**
     * Whether the rendered output is a Java source file. If true, the
     * processor will integrate it into the Java compilation process.
     *
     * @return true if the template generates a Java file
     */
    boolean javaFile() default false;
    
}
