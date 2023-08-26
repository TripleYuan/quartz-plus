package redcoder.quartzplus.schedcenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.PageResponse;
import redcoder.quartzplus.schedcenter.dto.sys.OperationLogDto;
import redcoder.quartzplus.schedcenter.dto.sys.OperationLogQuery;
import redcoder.quartzplus.schedcenter.service.OperationLogService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/operation-log")
@Api(tags = "操作日志")
public class OperationLogController {

    @Resource
    private OperationLogService operationLogService;

    @GetMapping("/list")
    @ApiOperation("获取操作日志列表")
    public ApiResult<PageResponse<OperationLogDto>> getList(OperationLogQuery query) {
        PageResponse<OperationLogDto> pageResponse = operationLogService.getList(query);
        return ApiResult.success(pageResponse);
    }
}
