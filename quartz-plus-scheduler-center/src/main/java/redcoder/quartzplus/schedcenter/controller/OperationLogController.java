package redcoder.quartzplus.schedcenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.PageResponse;
import redcoder.quartzplus.schedcenter.dto.system.OperationLogInfo;
import redcoder.quartzplus.schedcenter.dto.system.OperationLogQuery;
import redcoder.quartzplus.schedcenter.service.operationlog.NoAudit;
import redcoder.quartzplus.schedcenter.service.system.OperationLogService;

@RestController
@Api(tags = "操作日志")
public class OperationLogController {

    private OperationLogService operationLogService;

    @Autowired
    public OperationLogController(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    @GetMapping("/api/logs/operations")
    @ApiOperation("获取操作日志")
    @NoAudit
    public ApiResult<PageResponse<OperationLogInfo>> getOperationLogs(OperationLogQuery query) {
        PageResponse<OperationLogInfo> pageResponse = operationLogService.getOperationLogs(query);
        return ApiResult.success(pageResponse);
    }
}
