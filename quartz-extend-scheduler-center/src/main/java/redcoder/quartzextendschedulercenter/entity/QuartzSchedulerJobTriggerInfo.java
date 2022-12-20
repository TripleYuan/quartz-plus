package redcoder.quartzextendschedulercenter.entity;

import redcoder.quartzextendcore.core.dto.QuartzJobTriggerInfo;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "`quartz_scheduler_job_trigger_info`")
public class QuartzSchedulerJobTriggerInfo {
    /**
     * the name of scheduler
     */
    @Id
    @Column(name = "`sched_name`")
    private String schedName;

    /**
     * 与job相关联的触发器名称
     */
    @Id
    @Column(name = "`trigger_name`")
    private String triggerName;

    /**
     * 与job相关联的触发器所在组名称
     */
    @Id
    @Column(name = "`trigger_group`")
    private String triggerGroup;

    /**
     * job名称
     */
    @Column(name = "`job_name`")
    private String jobName;

    /**
     * job所在组名称
     */
    @Column(name = "`job_group`")
    private String jobGroup;

    /**
     * job的描述信息
     */
    @Column(name = "`job_desc`")
    private String jobDesc;

    /**
     * 触发器的描述信息
     */
    @Column(name = "`trigger_desc`")
    private String triggerDesc;

    /**
     * job的上一次执行时间
     */
    @Column(name = "`prev_fire_time`")
    private Date prevFireTime;

    /**
     * job的下一次执行时间
     */
    @Column(name = "`next_fire_time`")
    private Date nextFireTime;

    /**
     * 触发器的状态：NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED
     */
    @Column(name = "`trigger_state`")
    private String triggerState;

    @Column(name = "`create_time`")
    private Date createTime;

    @Column(name = "`update_time`")
    private Date updateTime;

    /**
     * 获取the name of scheduler
     *
     * @return sched_name - the name of scheduler
     */
    public String getSchedName() {
        return schedName;
    }

    /**
     * 设置the name of scheduler
     *
     * @param schedName the name of scheduler
     */
    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    /**
     * 获取与job相关联的触发器名称
     *
     * @return trigger_name - 与job相关联的触发器名称
     */
    public String getTriggerName() {
        return triggerName;
    }

    /**
     * 设置与job相关联的触发器名称
     *
     * @param triggerName 与job相关联的触发器名称
     */
    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    /**
     * 获取与job相关联的触发器所在组名称
     *
     * @return trigger_group - 与job相关联的触发器所在组名称
     */
    public String getTriggerGroup() {
        return triggerGroup;
    }

    /**
     * 设置与job相关联的触发器所在组名称
     *
     * @param triggerGroup 与job相关联的触发器所在组名称
     */
    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    /**
     * 获取job名称
     *
     * @return job_name - job名称
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * 设置job名称
     *
     * @param jobName job名称
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * 获取job所在组名称
     *
     * @return job_group - job所在组名称
     */
    public String getJobGroup() {
        return jobGroup;
    }

    /**
     * 设置job所在组名称
     *
     * @param jobGroup job所在组名称
     */
    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    /**
     * 获取job的描述信息
     *
     * @return job_desc - job的描述信息
     */
    public String getJobDesc() {
        return jobDesc;
    }

    /**
     * 设置job的描述信息
     *
     * @param jobDesc job的描述信息
     */
    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    /**
     * 获取触发器的描述信息
     *
     * @return trigger_desc - 触发器的描述信息
     */
    public String getTriggerDesc() {
        return triggerDesc;
    }

    /**
     * 设置触发器的描述信息
     *
     * @param triggerDesc 触发器的描述信息
     */
    public void setTriggerDesc(String triggerDesc) {
        this.triggerDesc = triggerDesc;
    }

    /**
     * 获取job的上一次执行时间
     *
     * @return prev_fire_time - job的上一次执行时间
     */
    public Date getPrevFireTime() {
        return prevFireTime;
    }

    /**
     * 设置job的上一次执行时间
     *
     * @param prevFireTime job的上一次执行时间
     */
    public void setPrevFireTime(Date prevFireTime) {
        this.prevFireTime = prevFireTime;
    }

    /**
     * 获取job的下一次执行时间
     *
     * @return next_fire_time - job的下一次执行时间
     */
    public Date getNextFireTime() {
        return nextFireTime;
    }

    /**
     * 设置job的下一次执行时间
     *
     * @param nextFireTime job的下一次执行时间
     */
    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    /**
     * 获取触发器的状态：NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED
     *
     * @return trigger_state - 触发器的状态：NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED
     */
    public String getTriggerState() {
        return triggerState;
    }

    /**
     * 设置触发器的状态：NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED
     *
     * @param triggerState 触发器的状态：NONE, NORMAL, PAUSED, COMPLETE, ERROR, BLOCKED
     */
    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public static QuartzSchedulerJobTriggerInfo valueOf(QuartzJobTriggerInfo origin) {
        QuartzSchedulerJobTriggerInfo info = new QuartzSchedulerJobTriggerInfo();
        // 属性赋值
        BeanUtils.copyProperties(origin, info);
        if (origin.getPrevFireTime() > 0) {
            info.setPrevFireTime(new Date(origin.getPrevFireTime()));
        }
        if (origin.getNextFireTime() > 0) {
            info.setNextFireTime(new Date(origin.getNextFireTime()));
        }
        return info;
    }
}