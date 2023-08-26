package redcoder.quartzplus.core.annotation;

import redcoder.quartzplus.core.core.DefaultQuartzKeyNameGenerator;
import redcoder.quartzplus.core.core.QuartzJobBeanPostProcessor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import redcoder.quartzplus.core.core.QuartzKeyNameGenerator;

import java.lang.annotation.*;

/**
 * 用于定义quartz job
 *
 * @author redcoder54
 * @see QuartzJobBeanPostProcessor
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface QuartzJob {

    /**
     * the name of Job's JobKey
     */
    String keyName() default "";

    /**
     * the group of Job's JobKey
     */
    String keyGroup() default Scheduler.DEFAULT_GROUP;

    /**
     * job's  description
     */
    String description() default "";

    /**
     * Whether or not the <code>Job</code> should remain stored after it is
     * orphaned (no <code>{@link Trigger}s</code> point to it).
     *
     * @see JobDetail#isDurable()
     */
    boolean storeDurably() default true;

    /**
     * Specifies the strategy indicates how to generate Quartz JobKey name and Trigger name.
     */
    Class<? extends QuartzKeyNameGenerator> quartzKeyNameGenerator() default DefaultQuartzKeyNameGenerator.class;
}
