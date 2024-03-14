package redcoder.quartzplus.schedcenter.service.operationlog;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationLog {

    private Integer userid;

    private String username;

    /**
     * api路径
     */
    private String apiPath;

    private String apiDesc;

    private String className;

    private String methodName;

    /**
     * api请求时间
     */
    private LocalDateTime requestTime;

    /**
     * 入参
     */
    private String inParams;

    /**
     * 出参
     */
    private String outParams;

    /**
     * api执行消耗时间
     */
    private long spendTimeMillis;
}
