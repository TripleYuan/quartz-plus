package redcoder.quartzextendschedulercenter.model.dto.instance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import redcoder.quartzextendschedulercenter.model.dto.BasePageRequest;

@Data
public class QueryInstanceInfo extends BasePageRequest {

    @ApiModelProperty("quartz实例名")
    private String schedName;
}
