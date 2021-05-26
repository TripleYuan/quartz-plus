package org.leekeggs.quartzextendcore.annotation;

import org.leekeggs.quartzextendcore.core.QuartzExtendConfig;
import org.leekeggs.quartzextendcore.core.QuartzJobBeanPostProcessor;
import org.leekeggs.quartzextendcore.core.QuartzJobRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动对quartz job的扫描，并自动注册到quartz scheduler
 *
 * @author leekeggs
 * @see QuartzJobRegistrar
 * @see QuartzJobBeanPostProcessor
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({QuartzJobRegistrar.class, QuartzExtendConfig.class})
public @interface QuartzJobScan {

    /**
     * Base packages to scan for quartz job
     */
    String[] value() default {};
}
