package redcoder.quartzplus.core.core;

import lombok.extern.slf4j.Slf4j;
import redcoder.quartzplus.core.annotation.QuartzJob;
import redcoder.quartzplus.core.annotation.QuartzTrigger;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.quartz.SchedulerAccessor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理{@link SchedulerFactoryBean}的BeanPostProcessor，存在{@link QuartzJob}注解的类都会被添加到QuartzScheduler中，
 * 如果类上同时存在{@link QuartzTrigger}，也会创建一个trigger，关联到job上。
 *
 * @author redcoder54
 * @since 1.0.0
 */
@Slf4j
public class QuartzJobBeanPostProcessor extends QuartzJobProcessorSupport implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (!(bean instanceof SchedulerFactoryBean)) {
            return bean;
        }
        try {
            SchedulerFactoryBean schedulerFactoryBean = (SchedulerFactoryBean) bean;

            // 如果jobFactory为空，则给它赋值
            setJobFactoryIfNull(schedulerFactoryBean);

            Class<SchedulerAccessor> schedulerAccessorClass = SchedulerAccessor.class;
            // 获取已有的JobDetail
            List<JobDetail> jobDetails = getExistingJobDetails(schedulerAccessorClass, schedulerFactoryBean);
            // 获取已有的Trigger
            List<Trigger> triggers = getExistingTriggers(schedulerAccessorClass, schedulerFactoryBean);

            String[] jobBeanNames = applicationContext.getBeanNamesForType(Job.class);
            for (String jobBeanName : jobBeanNames) {
                @SuppressWarnings("unchecked")
                Class<? extends Job> jobType = (Class<? extends Job>) applicationContext.getType(jobBeanName);
                if (jobType == null) {
                    continue;
                }
                QuartzJob quartzJob = AnnotationUtils.findAnnotation(jobType, QuartzJob.class);
                QuartzTrigger quartzTrigger = AnnotationUtils.findAnnotation(jobType, QuartzTrigger.class);
                if (quartzJob != null) {
                    // 创建JobDetail并添加到列表中
                    JobDetail jobDetail = JobBuilder.newJob(jobType)
                            .withIdentity(createJobKeyName(quartzJob, jobType), quartzJob.keyGroup())
                            .withDescription(quartzJob.description())
                            .storeDurably(quartzJob.storeDurably())
                            .build();
                    jobDetails.add(jobDetail);

                    if (quartzTrigger != null) {
                        // 创建job对应的trigger并添加到列表中
                        String cron = quartzTrigger.cron();
                        if (StringUtils.hasText(cron)) {
                            CronTrigger trigger = TriggerBuilder.newTrigger()
                                    .forJob(jobDetail.getKey())
                                    .withIdentity(createTriggerKeyName(quartzTrigger, jobType), quartzTrigger.keyGroup())
                                    .withDescription(quartzTrigger.description())
                                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                                    .build();
                            triggers.add(trigger);
                        } else {
                            log.warn("未指定cronExpress，不会创建job相关联的trigger");
                        }
                    }
                }
            }

            // 重新设置JobDetails和Triggers
            schedulerFactoryBean.setJobDetails(jobDetails.toArray(new JobDetail[0]));
            schedulerFactoryBean.setTriggers(triggers.toArray(new Trigger[0]));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return bean;
    }

    /**
     * 如果{@link SchedulerFactoryBean#jobFactory}为null，则将其设置为{@link SpringBeanJobFactory}
     */
    private void setJobFactoryIfNull(SchedulerFactoryBean schedulerFactoryBean) {
        try {
            Class<SchedulerFactoryBean> clazz = SchedulerFactoryBean.class;
            Field field = clazz.getDeclaredField("jobFactory");
            field.setAccessible(true);
            JobFactory jobFactory = (JobFactory) field.get(schedulerFactoryBean);
            if (jobFactory == null) {
                schedulerFactoryBean.setJobFactory(applicationContext.getBean(SpringBeanJobFactory.class));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private List<JobDetail> getExistingJobDetails(Class<SchedulerAccessor> schedulerAccessorClass,
                                                  SchedulerFactoryBean schedulerFactoryBean)
            throws NoSuchFieldException, IllegalAccessException {
        Field f1 = schedulerAccessorClass.getDeclaredField("jobDetails");
        f1.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<JobDetail> jobDetails = (List<JobDetail>) f1.get(schedulerFactoryBean);
        if (jobDetails == null) {
            jobDetails = new ArrayList<>();
        } else {
            jobDetails = new ArrayList<>(jobDetails);
        }
        return jobDetails;
    }

    private List<Trigger> getExistingTriggers(Class<SchedulerAccessor> schedulerAccessorClass,
                                              SchedulerFactoryBean schedulerFactoryBean)
            throws NoSuchFieldException, IllegalAccessException {
        Field f2 = schedulerAccessorClass.getDeclaredField("triggers");
        f2.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Trigger> triggers = (List<Trigger>) f2.get(schedulerFactoryBean);
        if (triggers == null) {
            triggers = new ArrayList<>();
        } else {
            triggers = new ArrayList<>(triggers);
        }
        return triggers;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
