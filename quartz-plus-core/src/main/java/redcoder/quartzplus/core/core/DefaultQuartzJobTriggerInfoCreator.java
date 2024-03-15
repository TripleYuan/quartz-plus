package redcoder.quartzplus.core.core;

import org.quartz.*;
import redcoder.quartzplus.core.core.dto.QuartzJobInfo;

import java.util.Optional;

public class DefaultQuartzJobTriggerInfoCreator implements QuartzJobTriggerInfoCreator {

    private Scheduler scheduler;

    public DefaultQuartzJobTriggerInfoCreator(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public QuartzJobInfo create(TriggerKey triggerKey) throws SchedulerException {
        String schedName = scheduler.getSchedulerName();
        Trigger trigger = scheduler.getTrigger(triggerKey);
        Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);

        QuartzJobInfo quartzJobInfo = new QuartzJobInfo();
        quartzJobInfo.setSchedName(schedName);
        // 设置Trigger相关属性
        quartzJobInfo.setTriggerName(triggerKey.getName());
        quartzJobInfo.setTriggerGroup(triggerKey.getGroup());
        quartzJobInfo.setTriggerDesc(trigger.getDescription());
        Optional.ofNullable(trigger.getPreviousFireTime()).ifPresent(t -> quartzJobInfo.setPrevFireTime(t.getTime()));
        Optional.ofNullable(trigger.getNextFireTime()).ifPresent(t -> quartzJobInfo.setNextFireTime(t.getTime()));
        quartzJobInfo.setTriggerState(triggerState.name());
        quartzJobInfo.setTriggerStateDesc(QuartzTriggerState.getDesc(triggerState.name()));
        if (trigger instanceof CronTrigger) {
            quartzJobInfo.setCron(((CronTrigger) trigger).getCronExpression());
        }

        // 设置job相关属性
        JobKey jobKey = trigger.getJobKey();
        if (jobKey != null) {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            quartzJobInfo.setJobName(jobDetail.getKey().getName());
            quartzJobInfo.setJobGroup(jobDetail.getKey().getGroup());
            quartzJobInfo.setJobDesc(jobDetail.getDescription());
        }
        return quartzJobInfo;
    }
}
