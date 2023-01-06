package redcoder.quartzextendschedulercenter.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "QuartzSchedulerOperationLog")
@Table(name = "quartz_scheduler_operation_log")
@Data
public class QuartzSchedulerOperationLog {

    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 操作人用户id
     */
    @Column(name = "`userid`")
    private Integer userid;

    /**
     * 操作人用户名
     */
    @Column(name = "`username`")
    private String username;

    /**
     * 访问的controller名称
     */
    @Column(name = "`controller`")
    private String controller;

    /**
     * 访问的方法名
     */
    @Column(name = "`method`")
    private String method;

    /**
     * 访问的接口信息
     */
    @Column(name = "`api_desc`")
    private String apiDesc;

    /**
     * 操作时间
     */
    @Column(name = "`operation_time`")
    private Date operationTime;
}
