package org.leekeggs.quartzextendschedulercenter.service;

import org.leekeggs.quartzextendschedulercenter.model.dto.tasklist.DeleteJobTriggerInfoDTO;
import org.leekeggs.quartzextendschedulercenter.model.dto.tasklist.JobTriggerInfoDTO;
import org.leekeggs.quartzextendschedulercenter.model.dto.tasklist.RefreshJobTriggerInfoDTO;
import org.leekeggs.quartzextendschedulercenter.model.dto.tasklist.TriggerJobDTO;

import java.util.List;

/**
 * 面向前端的API
 *
 * @author leekeggs
 * @since 2021-04-25
 */
public interface JobListService {

    /**
     * 获取所有的job、trigger信息
     */
    List<JobTriggerInfoDTO> getJobTriggerInfoList();

    /**
     * 刷新job和trigger信息
     *
     * @param dto 请求数据
     * @return 刷新后的数据
     */
    JobTriggerInfoDTO refreshJobTriggerInfo(RefreshJobTriggerInfoDTO dto);

    /**
     * 删除指定的job、trigger信息。
     * 注意：仅删除表中的数据，并不会删除真正的quartz实例中的job
     *
     * @param dto 请求数据
     * @return true - 删除成功
     */
    boolean delJobTriggerInfo(DeleteJobTriggerInfoDTO dto);

    /**
     * 触发job（执行job）
     *
     * @param dto 请求数据
     */
    void triggerJob(TriggerJobDTO dto);
}
