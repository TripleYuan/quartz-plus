package redcoder.quartzplus.schedcenter.service.system;

import redcoder.quartzplus.schedcenter.dto.PageResponse;
import redcoder.quartzplus.schedcenter.dto.system.OperationLogInfo;
import redcoder.quartzplus.schedcenter.dto.system.OperationLogQuery;

/**
 * 操作日志服务
 */
public interface OperationLogService {

    PageResponse<OperationLogInfo> getOperationLogs(OperationLogQuery query);
}
