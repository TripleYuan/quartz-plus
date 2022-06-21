package redcoder.quartzextendcore.core;

import org.quartz.Job;
import org.springframework.util.StringUtils;
import redcoder.quartzextendcore.annotation.QuartzJob;
import redcoder.quartzextendcore.annotation.QuartzTrigger;

/**
 * @author wxy
 * @since 2022-06-21
 */
public class QuartzJobProcessorSupport {

    private QuartzKeyNameGenerator generator = new DefaultQuartzKeyNameGenerator();

    public String createJobKeyName(QuartzJob quartzJob, Class<? extends Job> jobClass) {
        String name = quartzJob.jobKeyName();
        if (StringUtils.hasText(name)) {
            return name;
        }
        return generator.generateJobKeyName(jobClass);
    }

    public String createTriggerKeyName(QuartzTrigger quartzTrigger, Class<? extends Job> jobClass) {
        String name = quartzTrigger.triggerKeyName();
        if (StringUtils.hasText(name)) {
            return name;
        }
        return generator.generateTriggerKeyName(jobClass);
    }
}
