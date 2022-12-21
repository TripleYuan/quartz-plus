package redcoder.quartzextendschedulercenter.entity.key;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuartzSchedulerRoleMenuRelKey implements Serializable {
    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 菜单id
     */
    private Integer menuId;

    public QuartzSchedulerRoleMenuRelKey() {
    }

    public QuartzSchedulerRoleMenuRelKey(Integer roleId, Integer menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }
}
