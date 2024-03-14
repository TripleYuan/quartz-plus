package redcoder.quartzplus.schedcenter.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "QuartzPlusOperationLog")
@Table(name = "quartz_plus_operation_log")
@Data
public class QuartzPlusOperationLog {

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
     * api路径
     */
    @Column(name = "`api_path`")
    private String apiPath;

    /**
     * api描述
     */
    @Column(name = "`api_desc`")
    private String apiDesc;

    /**
     * 访问的controller名称
     */
    @Column(name = "`class_name`")
    private String className;

    /**
     * 访问的方法名
     */
    @Column(name = "`method_name`")
    private String methodName;

    /**
     * 输入输出参数信息id
     */
    @Column(name = "`params_id`")
    private Long paramsId;

    /**
     * 操作时间
     */
    @Column(name = "`request_time`")
    private LocalDateTime requestTime;

    /**
     * api执行消耗时间
     */
    @Column(name = "`spend_time_millis`")
    private Long spendTimeMillis;
}
