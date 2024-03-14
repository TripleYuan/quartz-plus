package redcoder.quartzplus.schedcenter.entity.key;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuartzPlusRoleMenuRelKey implements Serializable {
    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 菜单id
     */
    private Integer menuId;

    public QuartzPlusRoleMenuRelKey() {
    }

    public QuartzPlusRoleMenuRelKey(Integer roleId, Integer menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }
}
