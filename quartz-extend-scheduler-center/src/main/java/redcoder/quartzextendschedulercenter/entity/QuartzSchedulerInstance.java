package redcoder.quartzextendschedulercenter.entity;

import javax.persistence.*;

@Table(name = "`quartz_scheduler_instance`")
public class QuartzSchedulerInstance {
    /**
     * the name of scheduler
     */
    @Id
    @Column(name = "`sched_name`")
    private String schedName;

    /**
     * 实例主机地址
     */
    @Id
    @Column(name = "`instance_host`")
    private String instanceHost;

    /**
     * 实例服务端口
     */
    @Id
    @Column(name = "`instance_port`")
    private Integer instancePort;

    /**
     * 获取the name of scheduler
     *
     * @return sched_name - the name of scheduler
     */
    public String getSchedName() {
        return schedName;
    }

    /**
     * 设置the name of scheduler
     *
     * @param schedName the name of scheduler
     */
    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    /**
     * 获取实例主机地址
     *
     * @return instance_host - 实例主机地址
     */
    public String getInstanceHost() {
        return instanceHost;
    }

    /**
     * 设置实例主机地址
     *
     * @param instanceHost 实例主机地址
     */
    public void setInstanceHost(String instanceHost) {
        this.instanceHost = instanceHost;
    }

    /**
     * 获取实例服务端口
     *
     * @return instance_port - 实例服务端口
     */
    public Integer getInstancePort() {
        return instancePort;
    }

    /**
     * 设置实例服务端口
     *
     * @param instancePort 实例服务端口
     */
    public void setInstancePort(Integer instancePort) {
        this.instancePort = instancePort;
    }
}