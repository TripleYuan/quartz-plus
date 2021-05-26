package org.leekeggs.quartzextendcore.core;

import lombok.extern.slf4j.Slf4j;
import org.leekeggs.quartzextendcore.annotation.QuartzJob;
import org.leekeggs.quartzextendcore.annotation.QuartzJobScan;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 启动{@link ClassPathQuartzJobScanner}，扫描指定包下的class，将{@link QuartzJob}注解的类注册到spring容器中
 *
 * @author leekeggs
 * @since 1.0.0
 */
@Slf4j
public class QuartzJobRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private ResourceLoader resourceLoader;
    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes annoAttrs = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(QuartzJobScan.class.getName()));
        if (annoAttrs == null) {
            log.warn("缺失@QuartzJobScan");
            return;
        }
        List<String> basePackages = new ArrayList<>();
        for (String pkg : annoAttrs.getStringArray("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        ClassPathQuartzJobScanner jobScanner = new ClassPathQuartzJobScanner(registry);
        jobScanner.setResourceLoader(resourceLoader);
        jobScanner.setEnvironment(environment);
        jobScanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
            AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
            return metadata.isConcrete()
                    && metadata.hasAnnotation("org.leekeggs.quartzextendcore.annotation.QuartzJob");
        });

        if (!basePackages.isEmpty()) {
            jobScanner.scan(StringUtils.toStringArray(basePackages));
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
