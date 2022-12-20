package redcoder.quartzextendschedulercenter.dto.job;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("JobTriggerDTO")
public class JobTriggerDTO {

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
    private Date prevFireTime;

    /**
     * job的下一次执行时间
     */
    private Date nextFireTime;

    /**
     * 触发器的状态：NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED
     */
    private String triggerState;

    /**
     * 更新时间
     */
    private Date updateTime;
}
