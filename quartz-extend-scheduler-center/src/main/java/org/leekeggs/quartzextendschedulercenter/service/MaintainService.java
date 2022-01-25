package org.leekeggs.quartzextendschedulercenter.service;

import org.quartz.SchedulerException;

/**
 * 用于任务调度中心自身维护的服务
 *
 * @author redcoder54
 * @since 2021-04-27
 */
public interface MaintainService {

    /**
     * 注册调度器应用本身的实例
     */
    void registerOwnInstance() throws SchedulerException;

    /**
     * 注册调度器应用本身的job
     */
    void registerOwnJob() throws SchedulerException;
}
