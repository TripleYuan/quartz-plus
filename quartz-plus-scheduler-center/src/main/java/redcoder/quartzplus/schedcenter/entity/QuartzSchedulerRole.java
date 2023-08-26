package redcoder.quartzplus.schedcenter.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "`quartz_scheduler_role`")
@Data
@Entity(name = "QuartzSchedulerRole")
public class QuartzSchedulerRole implements Serializable {
    /**
     * 角色id
     */
    @Id
    @Column(name = "`role_id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    /**
     * 角色名称
     */
    @Column(name = "`role_name`")
    private String roleName;

    /**
     * 角色描述
     */
    @Column(name = "`role_desc`")
    private String roleDesc;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "`update_time`")
    private Date updateTime;
}