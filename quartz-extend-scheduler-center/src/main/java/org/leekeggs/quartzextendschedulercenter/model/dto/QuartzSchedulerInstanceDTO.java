package org.leekeggs.quartzextendschedulercenter.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("QuartzSchedulerInstanceDTO")
public class QuartzSchedulerInstanceDTO {
    /**
     * the name of scheduler
     */
    @NotBlank(message = "'schedName' must not be empty")
    private String schedName;

    /**
     * 实例主机地址
     */
    @NotBlank(message = "'instanceHost' must not be empty")
    private String instanceHost;

    /**
     * 实例服务端口
     */
    @NotNull(message = "'instanceHost' must not be null")
    private Integer instancePort;
}