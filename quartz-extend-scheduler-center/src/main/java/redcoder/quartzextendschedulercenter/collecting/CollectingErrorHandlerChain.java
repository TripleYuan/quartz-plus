package redcoder.quartzextendschedulercenter.collecting;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerInstance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 采集错误处理器链
 *
 * @author redcoder54
 */
@Component
public class CollectingErrorHandlerChain implements InitializingBean, ApplicationContextAware {

    private List<CollectingErrorHandler> handlers = new ArrayList<>(8);
    private ApplicationContext applicationContext;

    public List<CollectingErrorHandler> getHandlers() {
        return handlers;
    }

    public boolean handle(QuartzSchedulerInstance instance, Exception e) {
        for (CollectingErrorHandler handler : handlers) {
            if (handler.handle(instance, e)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        applicationContext.getBeansOfType(CollectingErrorHandler.class)
                .forEach((name, handler) -> {
                    handlers.add(handler);
                });
        handlers.sort(Comparator.comparing(CollectingErrorHandler::getOrder));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
