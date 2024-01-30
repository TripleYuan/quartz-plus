package redcoder.quartzplus.schedcenter.service;

import redcoder.quartzplus.schedcenter.dto.instance.QuartzInstanceDTO;

/**
 * quartz实例管理服务
 *
 * @author redcoder54
 * @since 2021-04-25
 */
public interface QuartzJobSchedulerService {

    /**
     * 添加实例
     *
     * @param dto 请求数据
     * @return true - 添加成功
     */
    boolean addInstance(QuartzInstanceDTO dto);

    /**
     * 删除实例，同时移除该实例下的所有job数据。
     * <p>
     * 注意：删除的是quartz_plus_job_trigger_info表中存储的job数据，而不是实例本身的job。
     *
     * @param dto 请求数据
     * @return true - 删除成功
     */
    boolean deleteInstance(QuartzInstanceDTO dto);
}
