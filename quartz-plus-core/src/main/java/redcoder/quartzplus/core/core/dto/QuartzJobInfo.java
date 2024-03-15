package redcoder.quartzplus.core.core.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 存储quartz中的job及其关联的trigger信息
 *
 * @author redcoder54
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class QuartzJobInfo {

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
     * 触发器的状态
     */
    private String triggerState;

    /**
     * 触发器的状态描述
     */
    private String triggerStateDesc;

    /**
     * cron表达式
     */
    private String cron;
}
