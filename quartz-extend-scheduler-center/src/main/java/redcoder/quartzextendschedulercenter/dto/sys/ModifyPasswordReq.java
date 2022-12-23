package redcoder.quartzextendschedulercenter.dto.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ModifyPasswordReq {

    @ApiModelProperty("旧密码")
    @NotEmpty(message = "参数oldPwd不能为空")
    private String oldPwd;

    @ApiModelProperty("新密码")
    @NotEmpty(message = "参数newPwd不能为空")
    private String newPwd;
}
