package redcoder.quartzplus.schedcenter.dto.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class RolePermissionInfo {

    @ApiModelProperty("角色id")
    @NotNull(message = "参数roleId不能为空")
    private Integer roleId;

    @ApiModelProperty("菜单id")
    @NotEmpty(message = "参数menuIds不能为空")
    private List<Integer> menuIds;
}
