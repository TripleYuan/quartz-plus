package org.leekeggs.quartzextendschedulercenter.service;

import org.quartz.SchedulerException;

/**
 * @author leekeggs
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
