package org.leekeggs.quartzextendschedulercenter.model.dto.instancelist;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.leekeggs.quartzextendschedulercenter.model.entity.QuartzSchedulerInstance;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author leekeggs
 * @since 2021-04-26
 */
@Data
@ApiModel("quartz实例model")
public class InstanceDTO {
    @ApiModelProperty("quartz实例名称")
    @NotBlank(message = "'schedName' must not be empty")
    private String schedName;
    @ApiModelProperty("实例主机地址")
    @NotBlank(message = "'instanceHost' must not be empty")
    private String instanceHost;
    @ApiModelProperty("实例服务端口")
    @NotNull(message = "'instancePort' must not be empty")
    private Integer instancePort;

    public static InstanceDTO valueOf(QuartzSchedulerInstance instance) {
        InstanceDTO instanceDTO = new InstanceDTO();
        BeanUtils.copyProperties(instance, instanceDTO);
        return instanceDTO;
    }
}
