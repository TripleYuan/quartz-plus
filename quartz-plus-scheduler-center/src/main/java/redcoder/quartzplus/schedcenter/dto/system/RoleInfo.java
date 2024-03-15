package redcoder.quartzplus.schedcenter.dto.system;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RoleInfo {

    private Integer roleId;

    @NotBlank(message = "参数roleName不能为空")
    private String roleName;

    @NotBlank(message = "参数roleDesc不能为空")
    private String roleDesc;

    public RoleInfo() {
    }

    public RoleInfo(Integer roleId, String roleName, String roleDesc) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleDesc = roleDesc;
    }
}
