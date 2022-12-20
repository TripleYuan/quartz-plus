package redcoder.quartzextendschedulercenter.service;

import redcoder.quartzextendschedulercenter.dto.PageResponse;
import redcoder.quartzextendschedulercenter.dto.job.*;

import java.util.List;

/**
 * 任务管理服务
 *
 * @author redcoder54
 * @since 2022-01-09
 */
public interface QuartzJobManageService {

    /**
     * 获取quartz实例名
     */
    List<String> getSchedNames();

    /**
     * 获取所有的job及其trigger信息
     */
    PageResponse<JobTriggerDTO> getJobTriggerInfos(QueryJobTriggerInfo queryJobTriggerInfo);

    /**
     * 刷新job和trigger信息
     *
     * @param dto 请求数据
     * @return 刷新后的数据
     */
    JobTriggerDTO refreshJobTrigger(RefreshJobTriggerDTO dto);

    /**
     * 删除本地保存的job数据
     */
    boolean removeLocal(RemoveLocalJobTriggerDTO removeLocalJobTriggerDTO);

    /**
     * 执行job
     *
     * @throws JobManageException 执行job失败
     */
    void triggerJob(JobManageDTO jobManageDTO);

    /**
     * 暂停job
     *
     * @throws JobManageException 暂停job失败
     */
    void pauseJob(JobManageDTO jobManageDTO);

    /**
     * 恢复（取消暂停）job
     *
     * @throws JobManageException 恢复job失败
     */
    void resumeJob(JobManageDTO jobManageDTO);

    /**
     * 删除job，从quartz实例中删除指定的job
     *
     * @throws JobManageException 删除job失败
     */
    void deleteJob(JobManageDTO jobManageDTO);
}
