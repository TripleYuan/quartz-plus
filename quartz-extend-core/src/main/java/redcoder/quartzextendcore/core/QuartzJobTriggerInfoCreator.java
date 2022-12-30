package redcoder.quartzextendcore.core;

import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import redcoder.quartzextendcore.core.dto.QuartzJobTriggerInfo;

/**
 * 用于创建{@link QuartzJobTriggerInfo}实例的创建器
 */
public interface QuartzJobTriggerInfoCreator {

    /**
     * 根据{@link TriggerKey}创建{@link QuartzJobTriggerInfo}实例
     */
    QuartzJobTriggerInfo create(TriggerKey triggerKey) throws SchedulerException;
}
