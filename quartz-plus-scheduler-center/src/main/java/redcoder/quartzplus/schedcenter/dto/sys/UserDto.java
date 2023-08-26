package redcoder.quartzplus.schedcenter.dto.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {

    @ApiModelProperty("用户id")
    private Integer userid;

    @ApiModelProperty("用户名")
    @NotEmpty(message = "参数username不能为空")
    private String username;

    @ApiModelProperty("用户类型，0-普通用户，1-管理员")
    @NotNull(message = "参数userType不能为空")
    private Integer userType;

    public UserDto() {
    }

    public UserDto(Integer userid, String username, Integer userType) {
        this.userid = userid;
        this.username = username;
        this.userType = userType;
    }
}
