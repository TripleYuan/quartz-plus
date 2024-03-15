package redcoder.quartzplus.schedcenter.dto.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PasswordUpdate {

    @ApiModelProperty("旧密码")
    @NotEmpty(message = "参数oldPwd不能为空")
    private String oldPwd;

    @ApiModelProperty("新密码")
    @NotEmpty(message = "参数newPwd不能为空")
    private String newPwd;
}
