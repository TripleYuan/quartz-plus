package redcoder.quartzplus.schedcenter.dto.job;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class JobExecutionRecord {

    /**
     * the name of scheduler
     */
    @NotEmpty(message = "schedName cannot be empty")
    private String schedName;

    /**
     * job名称
     */
    @NotEmpty(message = "jobName cannot be empty")
    private String jobName;

    /**
     * job所在组名称
     */
    @NotEmpty(message = "jobGroup cannot be empty")
    private String jobGroup;

    /**
     * 任务执行状态，1-执行中；2-执行成功；2-执行失败
     */
    @NotNull(message = "status cannot be null")
    private Integer status;

    /**
     * 任务开始时间, yyyy-MM-dd HH:mm:ss
     */
    @NotEmpty(message = "startTime cannot be empty")
    private String startTime;

    /**
     * 任务结束时间, yyyy-MM-dd HH:mm:ss
     */
    @NotEmpty(message = "endTime cannot be empty")
    private String endTime;

    /**
     * 任务执行耗时，毫秒
     */
    @NotNull(message = "costTime cannot be null")
    private Long costTime;

    /**
     * 失败信息
     */
    private String exception;
}
