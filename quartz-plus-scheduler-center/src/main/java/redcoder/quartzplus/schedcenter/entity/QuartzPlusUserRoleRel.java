package redcoder.quartzplus.schedcenter.entity;

import lombok.Data;
import redcoder.quartzplus.schedcenter.entity.key.QuartzPlusUserRoleRelKey;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "`quartz_plus_user_role_rel`")
@Data
@Entity(name = "QuartzSchedulerUserRoleRel")
@IdClass(QuartzPlusUserRoleRelKey.class)
public class QuartzPlusUserRoleRel implements Serializable {
    /**
     * 用户id
     */
    @Id
    @Column(name = "`userid`")
    private Integer userid;

    /**
     * 角色id
     */
    @Id
    @Column(name = "`role_id`")
    private Integer roleId;

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