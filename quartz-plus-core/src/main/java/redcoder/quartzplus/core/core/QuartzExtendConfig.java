package redcoder.quartzplus.core.core;

import org.quartz.Scheduler;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import redcoder.quartzplus.core.scheduler.DefaultQuartzService;
import redcoder.quartzplus.core.scheduler.QuartzController;
import redcoder.quartzplus.core.scheduler.QuartzJobSchedulerProperties;
import redcoder.quartzplus.core.scheduler.QuartzService;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 配置Quartz
 *
 * @author redcoder54
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(SchedulerFactoryBean.class)
@EnableConfigurationProperties(value = {QuartzProperties.class, QuartzJobSchedulerProperties.class})
public class QuartzExtendConfig {

    @Bean
    public SpringSingletonBeanJobFactory springBeanJobFactory(QuartzJobSchedulerProperties properties) {
        return new SpringSingletonBeanJobFactory(properties);
    }

    @Bean
    @ConditionalOnMissingBean(SchedulerFactoryBean.class)
    public SchedulerFactoryBean schedulerFactoryBean(QuartzProperties quartzProperties,
                                                     DataSource dataSource,
                                                     SpringBeanJobFactory jobFactory) {
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
        // set datasource
        schedulerFactoryBean.setDataSource(dataSource);

        return schedulerFactoryBean;
    }

    @Bean
    public QuartzJobTriggerInfoCreator quartzJobTriggerInfoCreator(Scheduler scheduler) {
        return new DefaultQuartzJobTriggerInfoCreator(scheduler);
    }

    @Bean
    public QuartzService quartzService(Scheduler scheduler,
                                       Environment env,
                                       QuartzJobSchedulerProperties properties,
                                       QuartzJobTriggerInfoCreator creator) {
        return new DefaultQuartzService(scheduler, env, properties, creator);
    }

    @Bean
    public QuartzController quartzController(QuartzService quartzService) {
        return new QuartzController(quartzService);
    }

    @Bean
    public QuartzJobBeanPostProcessor quartzJobBeanPostProcessor() {
        return new QuartzJobBeanPostProcessor();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public QuartzJobExecutionAdvisor quartzJobExecutionAdvisor(Environment env,
                                                               QuartzJobSchedulerProperties properties) {
        QuartzJobExecutionAdvisor advisor = new QuartzJobExecutionAdvisor();
        advisor.setAdvice(new QuartzJobExecutionInterceptor(env, properties));
        return advisor;
    }
}
