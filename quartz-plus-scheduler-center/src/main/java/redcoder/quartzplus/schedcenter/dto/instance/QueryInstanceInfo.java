package redcoder.quartzplus.schedcenter.dto.instance;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import redcoder.quartzplus.schedcenter.dto.BasePageRequest;

@Data
public class QueryInstanceInfo extends BasePageRequest {

    @ApiModelProperty("quartz实例名")
    private String schedName;
}
