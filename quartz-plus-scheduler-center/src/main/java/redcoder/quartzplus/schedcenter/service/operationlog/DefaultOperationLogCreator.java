package redcoder.quartzplus.schedcenter.service.operationlog;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import redcoder.quartzplus.common.utils.JsonUtils;
import redcoder.quartzplus.schedcenter.shiro.ShiroUtils;
import redcoder.quartzplus.schedcenter.utils.HttpServletUtils;

import java.lang.reflect.Method;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultOperationLogCreator implements OperationLogCreator {

    private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public OperationLog create(OperationLogContext context) {
        MethodInvocation invocation = context.getMethodInvocation();

        OperationLog model = new OperationLog();
        Optional.ofNullable(getUserid()).ifPresent(model::setUserid);
        Optional.ofNullable(getUsername()).ifPresent(model::setUsername);
        model.setApiPath(getApiPath());
        model.setApiDesc(getApiDesc(invocation.getMethod()));
        model.setRequestTime(context.getRequestTime());
        model.setClassName(invocation.getMethod().getDeclaringClass().getName());
        model.setMethodName(invocation.getMethod().getName());
        // 入参
        Object inParams = buildInParams(invocation);
        model.setInParams(JsonUtils.toJsonString(inParams));
        // 出参
        Object outParams = buildOutParams(context.getProceedResult());
        model.setOutParams(JsonUtils.toJsonString(outParams));
        // 执行耗时
        model.setSpendTimeMillis(context.getInvocationEndTimeMillis() - context.getInvocationStartTimeMillis());

        return model;
    }

    private Integer getUserid() {
        try {
            return ShiroUtils.getUserId();
        } catch (Exception e) {
            return null;
        }
    }

    private String getUsername() {
        try {
            return ShiroUtils.getUser().getUsername();
        } catch (Exception e) {
            return null;
        }
    }

    private String getApiPath() {
        try {
            return HttpServletUtils.getRequest().getRequestURI();
        } catch (Exception e) {
            return null;
        }
    }

    private String getApiDesc(Method method) {
        String apiDesc = "";
        Api api = AnnotationUtils.findAnnotation(method.getDeclaringClass(), Api.class);
        if (api != null) {
            apiDesc = apiDesc + api.tags()[0];
        }
        ApiOperation apiOperation = AnnotationUtils.findAnnotation(method, ApiOperation.class);
        if (apiOperation != null && apiOperation.value() != null) {
            apiDesc = apiDesc + "-" + apiOperation.value();
        }
        return apiDesc;
    }

    private Object buildInParams(MethodInvocation invocation) {
        Object[] arguments = invocation.getArguments();
        if (arguments.length == 0) {
            return Collections.emptyMap();
        }

        String[] parameterNames = parameterNameDiscoverer.getParameterNames(invocation.getMethod());
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < arguments.length; i++) {
            String parameterName = "arg" + (i + 1);
            if (parameterNames != null && parameterNames.length > i) {
                parameterName = parameterNames[i];
            }

            Object arg = arguments[i];
            if (canToJson(arg)) {
                params.put(parameterName, arg);
            } else {
                params.put(parameterName, arg.toString());
            }
        }
        return JsonUtils.toJsonString(params);
    }

    private String buildOutParams(Object proceedResult) {
        Object rtn = Optional.ofNullable(proceedResult).orElse(Collections.emptyMap());
        return JsonUtils.toJsonString(rtn);
    }

    private boolean canToJson(Object obj) {
        try {
            JsonUtils.toJsonString(obj);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
