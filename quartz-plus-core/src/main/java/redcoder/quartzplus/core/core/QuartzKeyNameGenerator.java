package redcoder.quartzplus.core.core;

import org.quartz.Job;

/**
 * The strategy interface indicates how to generate Quartz JobKey name and Trigger name.
 *
 * @author redcoder54
 * @since 1.1.0
 */
public interface QuartzKeyNameGenerator {

    String generateJobKeyName(Class<? extends Job> jobClass);

    String generateTriggerKeyName(Class<? extends Job> jobClass);
}
