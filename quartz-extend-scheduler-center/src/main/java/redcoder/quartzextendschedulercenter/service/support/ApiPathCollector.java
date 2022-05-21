package redcoder.quartzextendschedulercenter.service.support;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * api path收集器，收集指定controller包下的api，添加到api path存储中心
 *
 * @author redcoder54
 * @since 2021-05-22
 */
@Component
@Slf4j
public class ApiPathCollector implements BeanPostProcessor {

    private String targetPackage = "redcoder.quartzextendschedulercenter.controller";

    @Autowired
    private ApiPathRegister register;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        try {
            Class<?> clazz = bean.getClass();
            if (isCandidate(clazz)) {
                registerApiPath(clazz);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return bean;
    }

    private boolean isCandidate(Class<?> clazz) {
        String name = clazz.getName();
        return AnnotationUtils.isCandidateClass(clazz, Controller.class)
                && name.startsWith(targetPackage);
    }

    private void registerApiPath(Class<?> clazz) {
        List<ApiPathRegister.ApiPath> apiPaths = new ArrayList<>(10);

        RequestMapping requestMapping = AnnotationUtils.findAnnotation(clazz, RequestMapping.class);
        String pathPrefix = Optional.ofNullable(requestMapping).map(t -> t.value()[0]).orElse("");

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            ApiOperation operation = AnnotationUtils.findAnnotation(method, ApiOperation.class);
            String description = Optional.ofNullable(operation).map(ApiOperation::value).orElse("");

            RequestMapping rm = AnnotationUtils.findAnnotation(method, RequestMapping.class);
            Optional.ofNullable(rm).ifPresent(t -> addApiPath(rm.value(), apiPaths, pathPrefix, description));

            GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
            Optional.ofNullable(getMapping).ifPresent(t -> addApiPath(getMapping.value(), apiPaths, pathPrefix, description));

            PostMapping postMapping = AnnotationUtils.findAnnotation(method, PostMapping.class);
            Optional.ofNullable(postMapping).ifPresent(t -> addApiPath(postMapping.value(), apiPaths, pathPrefix, description));

            PutMapping putMapping = AnnotationUtils.findAnnotation(method, PutMapping.class);
            Optional.ofNullable(putMapping).ifPresent(t -> addApiPath(putMapping.value(), apiPaths, pathPrefix, description));

            DeleteMapping deleteMapping = AnnotationUtils.findAnnotation(method, DeleteMapping.class);
            Optional.ofNullable(deleteMapping).ifPresent(t -> addApiPath(deleteMapping.value(), apiPaths, pathPrefix, description));

            PatchMapping patchMapping = AnnotationUtils.findAnnotation(method, PatchMapping.class);
            Optional.ofNullable(patchMapping).ifPresent(t -> addApiPath(patchMapping.value(), apiPaths, pathPrefix, description));
        }

        register.addApiPath(apiPaths);
    }

    private void addApiPath(String[] mappingValue, List<ApiPathRegister.ApiPath> apiPaths, String pathPrefix, String description) {
        for (String s : mappingValue) {
            apiPaths.add(new ApiPathRegister.ApiPath(pathPrefix + s, description));
        }
    }
}
