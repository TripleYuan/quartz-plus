package redcoder.quartzextendcore.annotation;

import redcoder.quartzextendcore.core.DefaultQuartzKeyNameGenerator;
import redcoder.quartzextendcore.core.QuartzJobBeanPostProcessor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import redcoder.quartzextendcore.core.QuartzKeyNameGenerator;

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
    String jobKeyName() default "";

    /**
     * the group of Job's JobKey
     */
    String jobKeyGroup() default Scheduler.DEFAULT_GROUP;

    /**
     * job's  description
     */
    String jobDescription() default "";

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
