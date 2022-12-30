package redcoder.quartzextendcore.core.dto;

import lombok.Data;

@Data
public class ScheduleJob {

    /**
     * job名称
     */
    private String jobName;
    /**
     * job所在组名称
     */
    private String jobGroup;

    /**
     * cron表达式
     */
    private String cron;

}
