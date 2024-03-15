package redcoder.quartzplus.schedcenter.dto.job;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class JobInfoUpdate {

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

    /**
     * cron表达式
     */
    @NotBlank(message = "'cron' must not be empty")
    private String cron;
}
