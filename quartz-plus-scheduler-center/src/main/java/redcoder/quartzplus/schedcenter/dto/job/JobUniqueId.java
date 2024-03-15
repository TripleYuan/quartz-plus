package redcoder.quartzplus.schedcenter.dto.job;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author redcoder54
 * @since 2021-04-25
 */
@ApiModel("TriggerJobDTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobUniqueId {

    /**
     * the name of scheduler
     */
    @NotBlank(message = "'schedName' must not be empty")
    private String schedName;
    /**
     * job名称
     */
    @NotBlank(message = "'jobName' must not be empty")
    private String jobName;
    /**
     * job所在组名称
     */
    @NotBlank(message = "'jobGroup' must not be empty")
    private String jobGroup;
}
