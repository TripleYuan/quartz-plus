package org.leekeggs.quartzextendcore.core;

import org.leekeggs.quartzextendcore.annotation.QuartzJob;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 扫描指定包下的class，将{@link QuartzJob}注解的类注册到spring容器中
 *
 * @author redcoder54
 * @since 1.0.0
 */
public class ClassPathQuartzJobScanner extends ClassPathBeanDefinitionScanner {

    public ClassPathQuartzJobScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        AnnotationMetadata metadata = beanDefinition.getMetadata();
        return metadata.isConcrete()
                && metadata.hasAnnotation("org.leekeggs.quartzextendcore.annotation.QuartzJob");
    }
}
