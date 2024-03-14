package redcoder.quartzplus.schedcenter.service.operationlog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperationLogContext {

    private MethodInvocation methodInvocation;

    private LocalDateTime requestTime;

    private long invocationStartTimeMillis;

    private long invocationEndTimeMillis;

    @Nullable
    private Object proceedResult;
}
