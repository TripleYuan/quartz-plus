package redcoder.quartzplus.core.core;

import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import redcoder.quartzplus.core.core.dto.QuartzJobInfo;

/**
 * 用于创建{@link QuartzJobInfo}实例的创建器
 */
public interface QuartzJobTriggerInfoCreator {

    /**
     * 根据{@link TriggerKey}创建{@link QuartzJobInfo}实例
     */
    QuartzJobInfo create(TriggerKey triggerKey) throws SchedulerException;
}
