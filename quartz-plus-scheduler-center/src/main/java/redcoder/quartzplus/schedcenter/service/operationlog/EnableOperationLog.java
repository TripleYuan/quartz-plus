package redcoder.quartzplus.schedcenter.service.operationlog;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AutoProxyRegistrar;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({OperationLogConfiguration.class, AutoProxyRegistrar.class})
public @interface EnableOperationLog {

    boolean proxyTargetClass() default true;

    AdviceMode mode() default AdviceMode.ASPECTJ;

    int order() default Ordered.LOWEST_PRECEDENCE;
}
