package redcoder.quartzplus.schedcenter.entity.key;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuartzPlusUserRoleRelKey implements Serializable {

    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 角色id
     */
    private Integer roleId;

    public QuartzPlusUserRoleRelKey() {
    }

    public QuartzPlusUserRoleRelKey(Integer userid, Integer roleId) {
        this.userid = userid;
        this.roleId = roleId;
    }
}
