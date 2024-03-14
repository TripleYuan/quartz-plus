package redcoder.quartzplus.core.annotation;

import org.springframework.context.annotation.Import;
import redcoder.quartzplus.core.core.QuartzExtendConfig;
import redcoder.quartzplus.core.core.QuartzJobBeanPostProcessor;
import redcoder.quartzplus.core.core.QuartzJobRegistrar;

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
@Import({QuartzJobRegistrar.class, QuartzExtendConfig.class})
public @interface QuartzJobScan {

    /**
     * Base packages to scan for quartz job
     */
    String[] value() default {};
}
