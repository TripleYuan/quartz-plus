package redcoder.quartzplus.core.core;

import org.quartz.Job;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import redcoder.quartzplus.core.scheduler.QuartzJobSchedulerProperties;

import java.util.concurrent.ConcurrentHashMap;

public class SpringSingletonBeanJobFactory extends SpringBeanJobFactory {

    private final ConcurrentHashMap<Class<? extends Job>, Object> jobInstances = new ConcurrentHashMap<>();
    private QuartzJobSchedulerProperties properties;

    public SpringSingletonBeanJobFactory(QuartzJobSchedulerProperties properties) {
        this.properties = properties;
    }

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        if (!properties.isSingletonJob()) {
            return super.createJobInstance(bundle);
        }

        Class<? extends Job> jobClass = bundle.getJobDetail().getJobClass();
        Object jobInstance = jobInstances.get(jobClass);
        if (jobInstance == null) {
            synchronized (jobInstances) {
                jobInstance = jobInstances.get(jobClass);
                if (jobInstance == null) {
                    jobInstance = super.createJobInstance(bundle);
                    jobInstances.put(jobClass, jobInstance);
                }
            }
        }
        return jobInstance;
    }
}
