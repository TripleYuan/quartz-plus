package redcoder.quartzextendcore.scheduler;

import org.quartz.SchedulerException;
import redcoder.quartzextendcore.core.dto.QuartzJobTriggerInfo;
import redcoder.quartzextendcore.core.dto.ScheduleJob;

import java.util.List;

/**
 * quartz管理服务
 */
public interface QuartzService {

    /**
     * 获取当前quartz scheduler中的job和trigger信息
     *
     * @return 包含job和trigger信息 {@link QuartzJobTriggerInfo} 的集合
     */
    List<QuartzJobTriggerInfo> getQuartzJobTriggerInfoList() throws SchedulerException;

    /**
     * 根据trigger查询对应的 {@link QuartzJobTriggerInfo}
     *
     * @param triggerName  trigger's name
     * @param triggerGroup trigger's group
     * @return trigger对应的 {@link QuartzJobTriggerInfo}
     * @throws SchedulerException scheduler异常
     */
    QuartzJobTriggerInfo getQuartzJobTriggerInfo(String triggerName, String triggerGroup) throws SchedulerException;

    /**
     * 执行job
     *
     * @param jobName  job名称
     * @param jobGroup job所在组
     */
    void triggerJob(String jobName, String jobGroup) throws SchedulerException;

    /**
     * 暂停job
     *
     * @param jobName  job名称
     * @param jobGroup job所在组
     */
    void pauseJob(String jobName, String jobGroup) throws SchedulerException;

    /**
     * 恢复（取消暂停）job
     *
     * @param jobName  job名称
     * @param jobGroup job所在组
     */
    void resumeJob(String jobName, String jobGroup) throws SchedulerException;

    /**
     * 删除job
     *
     * @param jobName  job名称
     * @param jobGroup job所在组
     */
    void deleteJob(String jobName, String jobGroup) throws SchedulerException;

    /**
     * 修改任务(执行时间)
     */
    void scheduleJob(ScheduleJob scheduleJob) throws SchedulerException;
}
