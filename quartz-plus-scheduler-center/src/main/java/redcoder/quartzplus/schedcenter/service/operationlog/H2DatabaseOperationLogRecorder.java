package redcoder.quartzplus.schedcenter.service.operationlog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusOperationLog;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusOperationParams;
import redcoder.quartzplus.schedcenter.repository.OperationLogRepository;
import redcoder.quartzplus.schedcenter.repository.OperationParamsRepository;

@Slf4j
public class H2DatabaseOperationLogRecorder implements OperationLogRecorder {

    private OperationLogRepository logRepository;
    private OperationParamsRepository paramsRepository;

    public H2DatabaseOperationLogRecorder(OperationLogRepository logRepository,
                                          OperationParamsRepository paramsRepository) {
        this.logRepository = logRepository;
        this.paramsRepository = paramsRepository;
    }

    @Override
    @Transactional
    public void record(OperationLog operationLog) {
        try {
            // save operation params
            QuartzPlusOperationParams paramsEntity = new QuartzPlusOperationParams();
            paramsEntity.setInParams(operationLog.getInParams());
            paramsEntity.setOutParams(operationLog.getOutParams());
            paramsRepository.save(paramsEntity);

            // save operation log
            QuartzPlusOperationLog logEntity = new QuartzPlusOperationLog();
            logEntity.setUserid(operationLog.getUserid());
            logEntity.setUsername(operationLog.getUsername());
            logEntity.setApiPath(operationLog.getApiPath());
            logEntity.setApiDesc(operationLog.getApiDesc());
            logEntity.setClassName(operationLog.getClassName());
            logEntity.setMethodName(operationLog.getMethodName());
            logEntity.setParamsId(paramsEntity.getParamsId());
            logEntity.setRequestTime(operationLog.getRequestTime());
            logEntity.setSpendTimeMillis(operationLog.getSpendTimeMillis());
            logRepository.save(logEntity);
        } catch (Throwable e) {
            log.error("Failed to save operation log into h2database.", e);
        }
    }
}
