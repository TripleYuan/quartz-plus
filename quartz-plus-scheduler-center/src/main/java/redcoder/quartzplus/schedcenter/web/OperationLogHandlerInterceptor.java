package redcoder.quartzplus.schedcenter.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import redcoder.quartzplus.schedcenter.entity.QuartzSchedulerOperationLog;
import redcoder.quartzplus.schedcenter.repository.OperationLogRepository;
import redcoder.quartzplus.schedcenter.shiro.ShiroUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 记录用户操作日志
 */
@Component
@Slf4j
public class OperationLogHandlerInterceptor implements HandlerInterceptor, WebMvcConfigurer {

    private final Map<String, String> apiDescCache = new ConcurrentHashMap<>(256);
    private final OperationLogRepository repository;

    @Autowired
    public OperationLogHandlerInterceptor(OperationLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            if (!(handler instanceof HandlerMethod)) {
                return;
            }

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Class<?> controller = handlerMethod.getBeanType();
            if (!controller.getName().startsWith("redcoder.quartzextendschedulercenter.controller")) {
                return;
            }
            Method method = handlerMethod.getMethod();
            String apiDesc = getApiDesc(controller, method, handlerMethod);
            Integer userid = null;
            String username = "";
            try {
                userid = ShiroUtils.getUserId();
                username = ShiroUtils.getUser().getUsername();
            } catch (IllegalStateException e) {
                // ignore
            }
            // 记录操作日志
            log.info("用户[{}]正在访问系统. [userid={}, username={}, controller={}, method={}, apiDesc={}]",
                    username, userid, username, controller.getName(), method.getName(), apiDesc);
            QuartzSchedulerOperationLog record = new QuartzSchedulerOperationLog();
            record.setUserid(userid);
            record.setUsername(username);
            record.setController(controller.getName());
            record.setMethod(method.getName());
            record.setApiDesc(apiDesc);
            record.setOperationTime(new Date());
            repository.save(record);
        } catch (Exception e) {
            log.error("记录操作日志时发生错误", e);
        }
    }

    private String getApiDesc(Class<?> controller, Method method, HandlerMethod handlerMethod) {
        String key = controller.getName() + "#" + method.getName();
        synchronized (apiDescCache) {
            String apiDesc = this.apiDescCache.get(key);
            if (apiDesc == null) {
                Api api = AnnotationUtils.getAnnotation(controller, Api.class);
                if (api != null && api.tags().length > 0) {
                    apiDesc = api.tags()[0];
                }
                ApiOperation apiOperation = handlerMethod.getMethodAnnotation(ApiOperation.class);
                if (apiOperation != null && apiOperation.value() != null) {
                    apiDesc = apiDesc + "-" + apiOperation.value();
                    apiDescCache.put(key, apiDesc);
                }
            }
            return apiDesc;
        }
    }
}
