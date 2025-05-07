package com.cloudhopper.mc.annotations;

/*-
 * #%L
 * deployment-config-api - a library from the "Cloudhopper" project.
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


public final class ExtensionKeys {
    private ExtensionKeys() { /* no-instantiation */ }

    /** AWS-specific extensions */
    public static final class Aws {
        private Aws() {}

        /** x-aws-architecture */
        public static final class Architecture {
            private Architecture() {}

            /** The extension key */
            public static final String KEY = "x-aws-architecture";

            /** Allowed values for this key */
            public static final class Values {
                private Values() {}
                public static final String ARM64   = "arm64";
                public static final String X86_64  = "x86_64";
            }
        }

    }

}
