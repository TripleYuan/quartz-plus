package redcoder.quartzextendschedulercenter.dto.sys;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RoleDto {

    private Integer roleId;

    @NotBlank(message = "参数roleName不能为空")
    private String roleName;

    @NotBlank(message = "参数roleDesc不能为空")
    private String roleDesc;

    public RoleDto() {
    }

    public RoleDto(Integer roleId, String roleName, String roleDesc) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleDesc = roleDesc;
    }
}
