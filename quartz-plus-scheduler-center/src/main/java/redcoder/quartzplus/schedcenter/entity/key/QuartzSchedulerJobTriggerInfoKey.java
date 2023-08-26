package redcoder.quartzplus.schedcenter.entity.key;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuartzSchedulerJobTriggerInfoKey implements Serializable {

    /**
     * the name of scheduler
     */
    private String schedName;

    /**
     * 与job相关联的触发器名称
     */
    private String triggerName;

    /**
     * 与job相关联的触发器所在组名称
     */
    private String triggerGroup;

    public QuartzSchedulerJobTriggerInfoKey() {
    }

    public QuartzSchedulerJobTriggerInfoKey(String schedName, String triggerName, String triggerGroup) {
        this.schedName = schedName;
        this.triggerName = triggerName;
        this.triggerGroup = triggerGroup;
    }
}
