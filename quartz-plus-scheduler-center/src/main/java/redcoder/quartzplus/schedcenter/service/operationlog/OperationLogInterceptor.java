package redcoder.quartzplus.schedcenter.service.operationlog;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.time.LocalDateTime;

@Slf4j
public class OperationLogInterceptor implements MethodInterceptor {

    private OperationLogCreator creator;
    private OperationLogRecorder recorder;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        LocalDateTime requestTime = LocalDateTime.now();
        long s1 = System.currentTimeMillis();
        Object proceedResult = invocation.proceed();
        long s2 = System.currentTimeMillis();

        // 记录操作日志
        recordLog(invocation, requestTime, s1, s2, proceedResult);

        return proceedResult;
    }

    private void recordLog(MethodInvocation invocation, LocalDateTime requestTime, long s1, long s2, Object proceedResult) {
        try {
            OperationLogContext context = OperationLogContext.builder()
                    .methodInvocation(invocation)
                    .requestTime(requestTime)
                    .invocationStartTimeMillis(s1)
                    .invocationEndTimeMillis(s2)
                    .proceedResult(proceedResult)
                    .build();
            OperationLog operationLog = creator.create(context);
            recorder.record(operationLog);
        } catch (Exception e) {
            log.warn("记录操作日志异常", e);
        }
    }

    public void setCreator(OperationLogCreator creator) {
        this.creator = creator;
    }

    public void setRecorder(OperationLogRecorder recorder) {
        this.recorder = recorder;
    }
}
