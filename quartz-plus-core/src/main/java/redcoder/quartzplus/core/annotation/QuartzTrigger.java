package redcoder.quartzplus.core.annotation;

import redcoder.quartzplus.core.core.DefaultQuartzKeyNameGenerator;
import redcoder.quartzplus.core.core.QuartzJobBeanPostProcessor;
import org.quartz.Scheduler;
import redcoder.quartzplus.core.core.QuartzKeyNameGenerator;

import java.lang.annotation.*;

/**
 * 用于定义quartz job's trigger
 *
 * @author redcoder54
 * @see QuartzJobBeanPostProcessor
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface QuartzTrigger {

    /**
     * the name of Trigger's TriggerKey
     */
    String keyName() default "";

    /**
     * the group of Trigger's TriggerKey
     */
    String keyGroup() default Scheduler.DEFAULT_GROUP;

    /**
     * trigger's  description
     */
    String description() default "";

    /**
     * the cron expression string to base the schedule on
     */
    String cron();

    /**
     * Specifies the strategy indicates how to generate Quartz JobKey name and Trigger name.
     */
    Class<? extends QuartzKeyNameGenerator> quartzKeyNameGenerator() default DefaultQuartzKeyNameGenerator.class;
}
