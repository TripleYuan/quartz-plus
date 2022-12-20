package redcoder.quartzextendschedulercenter.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "`quartz_scheduler_user`")
@Data
public class QuartzSchedulerUser {
    /**
     * 用户id
     */
    @Id
    @Column(name = "`userid`")
    private Integer userid;

    /**
     * 用户名
     */
    @Column(name = "`username`")
    private String username;

    /**
     * 密码（真实密码的md5值）
     */
    @Column(name = "`password`")
    private String password;

    /**
     * 是否为管理员，0-否、1-是
     */
    @Column(name = "`is_admin`")
    private Boolean isAdmin;

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