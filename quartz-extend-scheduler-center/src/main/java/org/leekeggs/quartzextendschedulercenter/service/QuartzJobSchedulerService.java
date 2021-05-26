package org.leekeggs.quartzextendschedulercenter.service;

import org.leekeggs.quartzextendschedulercenter.exception.RefreshJobTriggerInfoException;
import org.leekeggs.quartzextendschedulercenter.exception.TriggerJobException;
import org.leekeggs.quartzextendschedulercenter.model.dto.QuartzSchedulerInstanceDTO;
import org.leekeggs.quartzextendschedulercenter.model.dto.tasklist.RefreshJobTriggerInfoDTO;
import org.leekeggs.quartzextendschedulercenter.model.dto.tasklist.TriggerJobDTO;

/**
 * @author leekeggs
 * @since 2021-04-25
 */
public interface QuartzJobSchedulerService {

    /**
     * 添加实例
     *
     * @param dto 请求数据
     * @return true - 添加成功
     */
    boolean addInstance(QuartzSchedulerInstanceDTO dto);

    /**
     * 添加实例
     *
     * @param dto 请求数据
     * @return true - 添加成功
     */
    boolean deleteInstance(QuartzSchedulerInstanceDTO dto);

    /**
     * 刷新job和trigger信息
     *
     * @param dto 刷新请求
     * @throws RefreshJobTriggerInfoException 刷新job和trigger信息失败
     */
    void refreshJobTriggerInfo(RefreshJobTriggerInfoDTO dto);

    /**
     * 触发job（执行job）
     *
     * @param dto 请求数据
     * @throws TriggerJobException 触发job失败
     */
    void triggerJob(TriggerJobDTO dto);
}
