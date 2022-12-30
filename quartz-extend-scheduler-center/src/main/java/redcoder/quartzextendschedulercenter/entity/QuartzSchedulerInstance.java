package redcoder.quartzextendschedulercenter.entity;

import lombok.Data;
import redcoder.quartzextendschedulercenter.entity.key.QuartzSchedulerInstanceKey;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "QuartzSchedulerInstance")
@Table(name = "`quartz_scheduler_instance`")
@IdClass(QuartzSchedulerInstanceKey.class)
@Data
public class QuartzSchedulerInstance implements Serializable {
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

    @Column(name = "`create_time`")
    private Date createTime;

    @Column(name = "`update_time`")
    private Date updateTime;
}