package redcoder.quartzplus.schedcenter.dto.sys;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class OperationLogDto {

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
