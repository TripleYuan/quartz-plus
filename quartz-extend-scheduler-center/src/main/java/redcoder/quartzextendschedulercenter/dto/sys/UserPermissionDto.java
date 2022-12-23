package redcoder.quartzextendschedulercenter.dto.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UserPermissionDto {

    @ApiModelProperty("用户id")
    @NotNull(message = "参数userid不能为空")
    private Integer userid;

    @ApiModelProperty("角色id")
    @NotEmpty(message = "参数roleIds不能为空")
    private List<Integer> roleIds;
}
