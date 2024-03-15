package redcoder.quartzplus.schedcenter.dto.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import redcoder.quartzplus.schedcenter.dto.BasePageRequest;

@Getter
@Setter
@ToString
public class OperationLogQuery extends BasePageRequest {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("操作时间（开始）yyyy-MM-dd HH:mm:ss")
    private String startTime;

    @ApiModelProperty("操作时间（结束）yyyy-MM-dd HH:mm:ss")
    private String endTime;

    @ApiModelProperty("接口路径")
    private String apiPath;
}
