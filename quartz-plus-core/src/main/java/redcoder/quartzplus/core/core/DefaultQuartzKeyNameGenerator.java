package redcoder.quartzplus.core.core;

import org.quartz.Job;

/**
 * @author redcoder54
 * @since 1.1.0
 */
public class DefaultQuartzKeyNameGenerator implements QuartzKeyNameGenerator {

    private static final String TRIGGER_KEY_NAME_SUFFIX = "Trigger";

    @Override
    public String generateJobKeyName(Class<? extends Job> jobClass) {
        return jobClass.getSimpleName();
    }

    @Override
    public String generateTriggerKeyName(Class<? extends Job> jobClass) {
        return jobClass.getSimpleName() + TRIGGER_KEY_NAME_SUFFIX;
    }
}
