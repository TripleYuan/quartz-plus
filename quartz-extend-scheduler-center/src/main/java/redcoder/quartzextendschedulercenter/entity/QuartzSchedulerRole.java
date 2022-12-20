package redcoder.quartzextendschedulercenter.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`quartz_scheduler_role`")
@Data
public class QuartzSchedulerRole {
    /**
     * 角色id
     */
    @Id
    @Column(name = "`role_id`")
    @GeneratedValue(generator = "JDBC")
    private Integer roleId;

    /**
     * 角色名称
     */
    @Column(name = "`role_name`")
    private String roleName;

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