package org.leekeggs.quartzextendschedulercenter.model.dto.tasklist;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author leekeggs
 * @since 2021-04-25
 */
@ApiModel("DeleteJobTriggerInfoDTO")
@Data
public class DeleteJobTriggerInfoDTO {
    /**
     * the name of scheduler
     */
    @NotBlank(message = "'schedName' must not be empty")
    private String schedName;
    /**
     * 与job相关联的触发器名称
     */
    @NotBlank(message = "'triggerName' must not be empty")
    private String triggerName;
    /**
     * 与job相关联的触发器所在组名称
     */
    @NotBlank(message = "'triggerGroup' must not be empty")
    private String triggerGroup;
}
