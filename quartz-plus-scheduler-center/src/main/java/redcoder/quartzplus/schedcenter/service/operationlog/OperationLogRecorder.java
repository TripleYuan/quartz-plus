package redcoder.quartzplus.schedcenter.service.operationlog;

/**
 * 操作日志记录器
 */
public interface OperationLogRecorder {

    /**
     * 记录操作日志
     *
     * @param operationLog 操作日志
     */
    void record(OperationLog operationLog);
}
