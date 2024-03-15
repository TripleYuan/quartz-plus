package redcoder.quartzplus.schedcenter.service;

import redcoder.quartzplus.schedcenter.dto.instance.QuartzInstanceInfo;

/**
 * 管理quartz调度器实例
 *
 * @author redcoder54
 * @since 2021-04-25
 */
public interface QuartzSchedulerInstanceService {

    /**
     * 添加实例
     *
     * @param dto 请求数据
     * @return true - 添加成功
     */
    boolean addInstance(QuartzInstanceInfo dto);

    /**
     * 移除实例，同时移除该实例下的所有job数据。
     * <p>
     * 注意：删除的是quartz_plus_job_trigger_info表中存储的job数据，而不是实例本身的job。
     *
     * @param dto 请求数据
     * @return true - 删除成功
     */
    boolean removeInstance(QuartzInstanceInfo dto);
}
