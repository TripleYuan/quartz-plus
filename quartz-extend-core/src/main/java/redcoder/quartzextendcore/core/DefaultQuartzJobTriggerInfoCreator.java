package redcoder.quartzextendcore.core;

import org.quartz.*;
import redcoder.quartzextendcore.core.dto.QuartzJobTriggerInfo;

import java.util.Optional;

public class DefaultQuartzJobTriggerInfoCreator implements QuartzJobTriggerInfoCreator {

    private Scheduler scheduler;

    public DefaultQuartzJobTriggerInfoCreator(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public QuartzJobTriggerInfo create(TriggerKey triggerKey) throws SchedulerException {
        String schedName = scheduler.getSchedulerName();
        Trigger trigger = scheduler.getTrigger(triggerKey);
        Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);

        QuartzJobTriggerInfo quartzJobTriggerInfo = new QuartzJobTriggerInfo();
        quartzJobTriggerInfo.setSchedName(schedName);
        // 设置Trigger相关属性
        quartzJobTriggerInfo.setTriggerName(triggerKey.getName());
        quartzJobTriggerInfo.setTriggerGroup(triggerKey.getGroup());
        quartzJobTriggerInfo.setTriggerDesc(trigger.getDescription());
        Optional.ofNullable(trigger.getPreviousFireTime()).ifPresent(t -> quartzJobTriggerInfo.setPrevFireTime(t.getTime()));
        Optional.ofNullable(trigger.getNextFireTime()).ifPresent(t -> quartzJobTriggerInfo.setNextFireTime(t.getTime()));
        quartzJobTriggerInfo.setTriggerState(triggerState.name());
        quartzJobTriggerInfo.setTriggerStateDesc(QuartzTriggerState.getDesc(triggerState.name()));
        if (trigger instanceof CronTrigger) {
            quartzJobTriggerInfo.setCron(((CronTrigger) trigger).getCronExpression());
        }

        // 设置job相关属性
        JobKey jobKey = trigger.getJobKey();
        if (jobKey != null) {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            quartzJobTriggerInfo.setJobName(jobDetail.getKey().getName());
            quartzJobTriggerInfo.setJobGroup(jobDetail.getKey().getGroup());
            quartzJobTriggerInfo.setJobDesc(jobDetail.getDescription());
        }
        return quartzJobTriggerInfo;
    }
}
