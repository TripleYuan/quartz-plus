package redcoder.quartzplus.schedcenter.service;

import redcoder.quartzplus.schedcenter.dto.PageResponse;
import redcoder.quartzplus.schedcenter.dto.job.*;
import redcoder.quartzplus.schedcenter.exception.JobOperationException;

import java.util.List;

/**
 * 管理Quartz任务
 *
 * @author redcoder54
 * @since 2022-01-09
 */
public interface QuartzJobService {

    /**
     * 获取quartz实例名
     */
    List<String> getSchedNames();

    /**
     * 获取所有的job及其trigger信息
     */
    PageResponse<JobInfo> getJobInfo(JobInfoQuery jobInfoQuery);

    /**
     * 修改job
     *
     * @throws JobOperationException 删除job失败
     */
    void updateJob(JobInfoUpdate jobInfoUpdate);

    /**
     * 删除job，从quartz实例中删除指定的job
     *
     * @throws JobOperationException 删除job失败
     */
    void deleteJob(JobUniqueId jobUniqueId);

    /**
     * 刷新job和trigger信息
     *
     * @param jobUniqueId job标识
     * @return 刷新后的数据
     */
    JobInfo refreshJob(JobUniqueId jobUniqueId);

    /**
     * 删除本地保存的job数据
     */
    void removeLocal(JobUniqueId jobUniqueId);

    /**
     * 执行job
     *
     * @throws JobOperationException 执行job失败
     */
    void executeJob(JobUniqueId jobUniqueId);

    /**
     * 暂停job
     *
     * @throws JobOperationException 暂停job失败
     */
    void pauseJob(JobUniqueId jobUniqueId);

    /**
     * 恢复（取消暂停）job
     *
     * @throws JobOperationException 恢复job失败
     */
    void resumeJob(JobUniqueId jobUniqueId);

    /**
     * 保存任务执行记录
     */
    void saveJobExecutionRecord(JobExecutionRecord record);

    /**
     * 查询任务执行记录
     */
    PageResponse<JobExecutionRecord> getJobExecutionRecord(JobInfoQuery dto);
}
