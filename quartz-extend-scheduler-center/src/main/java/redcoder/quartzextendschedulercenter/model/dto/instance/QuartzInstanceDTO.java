package redcoder.quartzextendschedulercenter.model.dto.instance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import redcoder.quartzextendschedulercenter.model.entity.QuartzSchedulerInstance;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("QuartzInstanceDTO")
public class QuartzInstanceDTO {
    /**
     * the name of scheduler
     */
    @ApiModelProperty("quartz实例名称")
    @NotBlank(message = "'schedName' must not be empty")
    private String schedName;

    /**
     * 实例主机地址
     */
    @ApiModelProperty("实例主机地址")
    @NotBlank(message = "'instanceHost' must not be empty")
    private String instanceHost;

    /**
     * 实例服务端口
     */
    @ApiModelProperty("实例服务端口")
    @NotNull(message = "'instanceHost' must not be null")
    private Integer instancePort;

    public static QuartzInstanceDTO valueOf(QuartzSchedulerInstance instance) {
        QuartzInstanceDTO dto = new QuartzInstanceDTO();
        BeanUtils.copyProperties(instance, dto);
        return dto;
    }
}