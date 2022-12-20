package redcoder.quartzextendschedulercenter.dto.instance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import redcoder.quartzextendschedulercenter.dto.BasePageRequest;

@Data
public class QueryInstanceInfo extends BasePageRequest {

    @ApiModelProperty("quartz实例名")
    private String schedName;
}
