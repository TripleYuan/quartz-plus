package redcoder.quartzplus.schedcenter.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "QuartzPlusJobExecutionRecord")
@Table(name = "`quartz_plus_job_execution_record`")
@Data
public class QuartzPlusJobExecutionRecord {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * the name of scheduler
     */
    @Column(name = "`sched_name`")
    private String schedName;


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
     * 任务执行状态，1-执行中；2-执行成功；2-执行失败
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 任务开始时间
     */
    @Column(name = "start_time")
    private LocalDateTime startTime;

    /**
     * 任务结束时间
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * 任务执行耗时，毫秒
     */
    @Column(name = "cost_time")
    private Long costTime;

    /**
     * 失败信息
     */
    @Column(name = "exception")
    private String exception;
}
