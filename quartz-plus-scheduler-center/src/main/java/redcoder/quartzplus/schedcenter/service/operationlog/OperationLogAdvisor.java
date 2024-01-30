package redcoder.quartzplus.schedcenter.service.operationlog;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

public class OperationLogAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private final ApiLogPointcut pointcut = new ApiLogPointcut();

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    public static class ApiLogPointcut extends StaticMethodMatcherPointcut {
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            return AnnotationUtils.findAnnotation(method, RequestMapping.class) != null;
        }
    }
}