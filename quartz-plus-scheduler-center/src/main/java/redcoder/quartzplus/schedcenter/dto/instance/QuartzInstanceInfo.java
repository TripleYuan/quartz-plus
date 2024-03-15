package redcoder.quartzplus.schedcenter.dto.instance;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusInstance;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("QuartzInstanceDTO")
@NoArgsConstructor
@AllArgsConstructor
public class QuartzInstanceInfo {
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

    public static QuartzInstanceInfo valueOf(QuartzPlusInstance instance) {
        QuartzInstanceInfo dto = new QuartzInstanceInfo();
        BeanUtils.copyProperties(instance, dto);
        return dto;
    }
}