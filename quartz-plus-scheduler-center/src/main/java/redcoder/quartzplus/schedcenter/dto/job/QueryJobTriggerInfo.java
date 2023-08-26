package redcoder.quartzplus.schedcenter.dto.job;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import redcoder.quartzplus.schedcenter.dto.BasePageRequest;

@Data
public class QueryJobTriggerInfo extends BasePageRequest {

    @ApiModelProperty("quartz实例名")
    private String schedName;

    @ApiModelProperty("任务名（支持模糊查询）")
    private String jobName;
}
