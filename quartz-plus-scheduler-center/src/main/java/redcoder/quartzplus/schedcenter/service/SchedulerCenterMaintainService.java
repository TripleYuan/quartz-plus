package redcoder.quartzplus.schedcenter.service;

/**
 * 用于任务调度中心自身维护的服务
 *
 * @author redcoder54
 * @since 2021-04-27
 */
public interface SchedulerCenterMaintainService {

    /**
     * 将自己注册到调度中心
     */
    void register();
}
