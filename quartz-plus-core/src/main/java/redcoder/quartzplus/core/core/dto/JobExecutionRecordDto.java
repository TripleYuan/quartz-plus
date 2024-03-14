package redcoder.quartzplus.core.core.dto;

public class JobExecutionRecordDto {

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
     * 任务执行状态，1-执行中；2-执行成功；2-执行失败
     */
    private Integer status;

    /**
     * 任务开始时间, yyyy-MM-dd HH:mm:ss
     */
    private String startTime;

    /**
     * 任务结束时间, yyyy-MM-dd HH:mm:ss
     */
    private String endTime;

    /**
     * 任务执行耗时，毫秒
     */
    private Long costTime;

    /**
     * 失败信息
     */
    private String exception;


    public String getSchedName() {
        return schedName;
    }

    public JobExecutionRecordDto setSchedName(String schedName) {
        this.schedName = schedName;
        return this;
    }

    public String getJobName() {
        return jobName;
    }

    public JobExecutionRecordDto setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public JobExecutionRecordDto setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public JobExecutionRecordDto setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getStartTime() {
        return startTime;
    }

    public JobExecutionRecordDto setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public String getEndTime() {
        return endTime;
    }

    public JobExecutionRecordDto setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public Long getCostTime() {
        return costTime;
    }

    public JobExecutionRecordDto setCostTime(Long costTime) {
        this.costTime = costTime;
        return this;
    }

    public String getException() {
        return exception;
    }

    public JobExecutionRecordDto setException(String exception) {
        this.exception = exception;
        return this;
    }
}
