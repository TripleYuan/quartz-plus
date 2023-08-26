package redcoder.quartzplus.schedcenter.service;

import redcoder.quartzplus.schedcenter.dto.PageResponse;
import redcoder.quartzplus.schedcenter.dto.sys.OperationLogDto;
import redcoder.quartzplus.schedcenter.dto.sys.OperationLogQuery;

/**
 * 操作日志服务
 */
public interface OperationLogService {

    PageResponse<OperationLogDto> getList(OperationLogQuery query);
}
