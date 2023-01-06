package redcoder.quartzextendschedulercenter.service;

import redcoder.quartzextendschedulercenter.dto.PageResponse;
import redcoder.quartzextendschedulercenter.dto.sys.OperationLogDto;
import redcoder.quartzextendschedulercenter.dto.sys.OperationLogQuery;

/**
 * 操作日志服务
 */
public interface OperationLogService {

    PageResponse<OperationLogDto> getList(OperationLogQuery query);
}
