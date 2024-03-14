package redcoder.quartzplus.schedcenter.service.operationlog;

import lombok.extern.slf4j.Slf4j;
import redcoder.quartzplus.common.utils.JsonUtils;

@Slf4j
public class LoggerOperationLogRecorder implements OperationLogRecorder {

    @Override
    public void record(OperationLog operationLog) {
        try {
            String message = JsonUtils.toJsonString(operationLog);
            log.debug(message);
        } catch (Exception e) {
            log.debug(String.valueOf(operationLog));
        }
    }
}
