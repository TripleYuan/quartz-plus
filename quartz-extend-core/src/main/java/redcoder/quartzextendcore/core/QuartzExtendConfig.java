package redcoder.quartzextendcore.core;

import redcoder.quartzextendcore.scheduler.QuartzController;
import redcoder.quartzextendcore.scheduler.QuartzJobSchedulerProperties;
import redcoder.quartzextendcore.scheduler.DefaultQuartzService;
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
import redcoder.quartzextendcore.scheduler.QuartzService;

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
@EnableConfigurationProperties(QuartzProperties.class)
public class QuartzExtendConfig {

    @Bean
    @ConditionalOnMissingBean(SpringBeanJobFactory.class)
    public SpringBeanJobFactory springBeanJobFactory() {
        return new SpringBeanJobFactory();
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
    public static QuartzJobBeanPostProcessor quartzJobBeanPostProcessor() {
        return new QuartzJobBeanPostProcessor();
    }
}
