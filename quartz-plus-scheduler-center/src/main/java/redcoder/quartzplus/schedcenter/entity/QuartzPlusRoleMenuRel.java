package redcoder.quartzplus.schedcenter.entity;

import lombok.Data;
import redcoder.quartzplus.schedcenter.entity.key.QuartzPlusRoleMenuRelKey;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Table(name = "`quartz_plus_role_menu_rel`")
@Data
@Entity(name = "QuartzSchedulerRoleMenuRel")
@IdClass(QuartzPlusRoleMenuRelKey.class)
public class QuartzPlusRoleMenuRel implements Serializable {

    /**
     * 角色id
     */
    @Id
    @Column(name = "`role_id`")
    private Integer roleId;

    /**
     * 菜单id
     */
    @Id
    @Column(name = "`menu_id`")
    private Integer menuId;


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