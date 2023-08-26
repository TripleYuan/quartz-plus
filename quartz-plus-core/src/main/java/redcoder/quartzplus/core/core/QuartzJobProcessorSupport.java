package redcoder.quartzplus.core.core;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.springframework.util.StringUtils;
import redcoder.quartzplus.core.annotation.QuartzJob;
import redcoder.quartzplus.core.annotation.QuartzTrigger;

/**
 * @author wxy
 * @since 2022-06-21
 */
@Slf4j
public class QuartzJobProcessorSupport {

    private final QuartzKeyNameGenerator defaultQuartzKeyNameGenerator = new DefaultQuartzKeyNameGenerator();

    public String createJobKeyName(QuartzJob quartzJob, Class<? extends Job> jobClass) {
        String name = quartzJob.keyName();
        if (StringUtils.hasText(name)) {
            return name;
        }
        return createGenerator(quartzJob.quartzKeyNameGenerator()).generateJobKeyName(jobClass);
    }

    public String createTriggerKeyName(QuartzTrigger quartzTrigger, Class<? extends Job> jobClass) {
        String name = quartzTrigger.keyName();
        if (StringUtils.hasText(name)) {
            return name;
        }
        return createGenerator(quartzTrigger.quartzKeyNameGenerator()).generateTriggerKeyName(jobClass);
    }

    private QuartzKeyNameGenerator createGenerator(Class<? extends QuartzKeyNameGenerator> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            log.warn("Failed to create QuartzKeyNameGenerator, we will use DefaultQuartzKeyNameGenerator.", e);
            return defaultQuartzKeyNameGenerator;
        }
    }
}
