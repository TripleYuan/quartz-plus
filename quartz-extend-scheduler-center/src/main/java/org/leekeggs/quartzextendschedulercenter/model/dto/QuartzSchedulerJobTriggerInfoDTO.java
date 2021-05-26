package org.leekeggs.quartzextendschedulercenter.model.dto;

import lombok.Data;

/**
 * @author leekeggs
 * @since 2021-04-25
 */
@Data
public class QuartzSchedulerJobTriggerInfoDTO {
    /**
     * the name of scheduler
     */
    private String schedName;
    /**
     * job名称
     */
    private String jobName;
    /**
     * job所在组名称
     */
    private String jobGroup;
    /**
     * job的描述信息
     */
    private String jobDesc;
    /**
     * 与job相关联的触发器名称
     */
    private String triggerName;
    /**
     * 与job相关联的触发器所在组名称
     */
    private String triggerGroup;
    /**
     * 触发器的描述信息
     */
    private String triggerDesc;
    /**
     * job的上一次执行时间
     */
    private long prevFireTime = -1;
    /**
     * job的下一次执行时间
     */
    private long nextFireTime = -1;

    /**
     * 触发器的状态：NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED
     */
    private String triggerState;
}
