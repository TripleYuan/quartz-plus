package redcoder.quartzextendschedulercenter.model.dto.job;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author redcoder54
 * @since 2021-04-25
 */
@ApiModel("RemoveLocalJobTriggerDTO")
@Data
public class RemoveLocalJobTriggerDTO {
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
