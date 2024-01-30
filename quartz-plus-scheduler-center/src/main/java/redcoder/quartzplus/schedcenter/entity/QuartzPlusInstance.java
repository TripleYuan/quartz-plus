package redcoder.quartzplus.schedcenter.entity;

import lombok.Data;
import redcoder.quartzplus.schedcenter.entity.key.QuartzPlusInstanceKey;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "QuartzSchedulerInstance")
@Table(name = "`quartz_plus_instance`")
@IdClass(QuartzPlusInstanceKey.class)
@Data
public class QuartzPlusInstance implements Serializable {
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