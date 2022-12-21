package redcoder.quartzextendschedulercenter.entity.key;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuartzSchedulerUserRoleRelKey implements Serializable {

    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 角色id
     */
    private Integer roleId;

    public QuartzSchedulerUserRoleRelKey() {
    }

    public QuartzSchedulerUserRoleRelKey(Integer userid, Integer roleId) {
        this.userid = userid;
        this.roleId = roleId;
    }
}
