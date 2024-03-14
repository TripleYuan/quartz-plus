package redcoder.quartzplus.core.core;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;
import java.util.Objects;

public class QuartzJobExecutionAdvisor extends AbstractBeanFactoryPointcutAdvisor {
    @Override
    public Pointcut getPointcut() {
        return new Pointcut() {
            @Override
            public ClassFilter getClassFilter() {
                return new ClassFilter() {
                    @Override
                    public boolean matches(Class<?> clazz) {
                        return Job.class.isAssignableFrom(clazz) || QuartzJobBean.class.isAssignableFrom(clazz);
                    }
                };
            }

            @Override
            public MethodMatcher getMethodMatcher() {
                return new StaticMethodMatcher() {
                    @Override
                    public boolean matches(Method method, Class<?> targetClass) {
                        String methodName = method.getName();
                        int parameterCount = method.getParameterCount();
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        return (Objects.equals("execute", methodName) || Objects.equals("executeInternal", methodName))
                                && parameterCount == 1
                                && parameterTypes[0].isAssignableFrom(JobExecutionContext.class);
                    }
                };
            }
        };
    }

}
