package org.leekeggs.quartzextendcore.core;

import org.leekeggs.quartzextendcore.scheduler.QuartzController;
import org.leekeggs.quartzextendcore.scheduler.QuartzService;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.util.Properties;

/**
 * 配置Quartz
 *
 * @author leekeggs
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(SchedulerFactoryBean.class)
@EnableConfigurationProperties(QuartzProperties.class)
public class QuartzExtendConfig {

    @Bean
    @ConditionalOnMissingBean(SpringBeanJobFactory.class)
    public SpringBeanJobFactory springBeanJobFactory() {
        return new SpringBeanJobFactory();
    }

    @Bean
    @ConditionalOnMissingBean(SchedulerFactoryBean.class)
    public SchedulerFactoryBean schedulerFactoryBean(QuartzProperties quartzProperties, SpringBeanJobFactory jobFactory) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        // add properties
        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());
        // auto startup
        schedulerFactoryBean.setAutoStartup(quartzProperties.isAutoStartup());
        // delay startup
        schedulerFactoryBean.setStartupDelay((int) quartzProperties.getStartupDelay().getSeconds());
        // overwrite existing jobs
        schedulerFactoryBean.setOverwriteExistingJobs(quartzProperties.isOverwriteExistingJobs());
        // set properties
        schedulerFactoryBean.setQuartzProperties(properties);
        // set job factory
        schedulerFactoryBean.setJobFactory(jobFactory);

        return schedulerFactoryBean;
    }

    @Bean
    public QuartzService quartzService(Scheduler scheduler, Environment env) {
        return new QuartzService(scheduler, env);
    }

    @Bean
    public QuartzController quartzController(QuartzService quartzService) {
        return new QuartzController(quartzService);
    }

    @Bean
    public static QuartzJobBeanPostProcessor quartzJobBeanPostProcessor() {
        return new QuartzJobBeanPostProcessor();
    }
}
