package eu.cloudhopper.mc.annotations;

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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.SOURCE;
import java.lang.annotation.Target;

/**
 * Declares that the annotated function should be triggered on a schedule.
 * <p>
 * Can be combined with {@link Function} to define a time-based trigger using a cron expression.
 *
 * <h2>Example</h2>
 * <pre>{@code
 @Function(name = "dailyTask")
 @ScheduledTrigger(cron = "0 0 * * *")
 public class DailyJob implements CloudRequestHandler<Void, Void> {
     public Void handleRequest(Void input, HandlerContext context) {
         // do something daily
         return null;
     }
 }
 }</pre>
 */
@Retention(SOURCE)
@Target(ElementType.METHOD)
public @interface ScheduledTrigger {

    /**
     * A cron expression defining the schedule at which the function should run.
     * Uses standard cron syntax (e.g., "0 0 * * *" for daily at midnight).
     *
     * @return cron expression
     */
    String cron();

    /**
     * The timezone to interpret the cron expression in.
     * Defaults to UTC.
     *
     * @return IANA timezone identifier (e.g., "UTC", "Europe/Berlin")
     */
    String timezone() default "UTC";
    
    
    /**
     * Constants representing the attribute names of {@link Function}.These are intended for use by generator implementors when declaring which
 attributes their generator supports in {see: eu.cloudhopper.mc.generator.api.annotations.GeneratorFeature}.
     *
     * Example usage:
 <pre>{@code
 @GeneratorFeature(
     supportedAnnotation = ScheduledTrigger.class,
     supportedAttributes = {
         ScheduleAttribute.CRON,
     }
 )
 }</pre>
     *
     * This class is not relevant for application developers and may be ignored
     * in function code.
     */
    public static class ScheduleAttribute{
        public static final String CRON = "cron";
        public static final String TIMEZONE = "timezone";
    }
}
