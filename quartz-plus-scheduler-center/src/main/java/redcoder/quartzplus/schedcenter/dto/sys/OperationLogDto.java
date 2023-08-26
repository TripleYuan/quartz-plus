package redcoder.quartzplus.schedcenter.dto.sys;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class OperationLogDto {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("controller")
    private String controller;

    @ApiModelProperty("method")
    private String method;

    @ApiModelProperty("api描述")
    private String apiDesc;

    @ApiModelProperty("操作时间")
    private Date operationTime;
}
