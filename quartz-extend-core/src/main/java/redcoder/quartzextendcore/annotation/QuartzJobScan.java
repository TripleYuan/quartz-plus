package redcoder.quartzextendcore.annotation;

import redcoder.quartzextendcore.core.QuartzExtendConfig;
import redcoder.quartzextendcore.core.QuartzJobBeanPostProcessor;
import redcoder.quartzextendcore.core.QuartzJobRegistrar;
import redcoder.quartzextendcore.scheduler.QuartzJobSchedulerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动对quartz job的扫描，并自动注册到quartz scheduler
 *
 * @author redcoder54
 * @see QuartzJobRegistrar
 * @see QuartzJobBeanPostProcessor
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableConfigurationProperties(QuartzJobSchedulerProperties.class)
@Import({QuartzJobRegistrar.class, QuartzExtendConfig.class})
public @interface QuartzJobScan {

    /**
     * Base packages to scan for quartz job
     */
    String[] value() default {};
}
