package redcoder.quartzplus.schedcenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import redcoder.quartzplus.schedcenter.constant.ApiStatus;
import redcoder.quartzplus.schedcenter.dto.job.*;
import redcoder.quartzplus.schedcenter.exception.JobManageException;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.PageResponse;
import redcoder.quartzplus.schedcenter.service.QuartzJobManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author redcoder54
 * @since 2022-01-09
 */
@RestController
@RequestMapping("/api/job")
@Api(tags = "Quartz任务管理")
@Slf4j
public class QuartzJobController {

    @Autowired
    private QuartzJobManageService jobManageService;

    @GetMapping("/sched-names")
    @ApiOperation(value = "获取quartz实例名称", httpMethod = "GET")
    public ApiResult<List<String>> getSchedNames() {
        return ApiResult.success(jobManageService.getSchedNames());
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取job", httpMethod = "GET")
    public ApiResult<PageResponse<JobTriggerDTO>> getJobTriggerInfoList(QueryJobTriggerInfo queryJobTriggerInfo) {
        return ApiResult.success(jobManageService.getJobTriggerInfos(queryJobTriggerInfo));
    }

    @PostMapping("/refresh")
    @ApiOperation(value = "刷新job信息", httpMethod = "POST")
    public ApiResult<JobTriggerDTO> refreshJobTrigger(@Valid @RequestBody RefreshJobTriggerDTO dto) {
        return ApiResult.success(jobManageService.refreshJobTrigger(dto));
    }

    @PostMapping("/removeLocal")
    @ApiOperation(value = "删除本地保存的job数据", httpMethod = "DELETE")
    public ApiResult<String> removeLocal(@Valid @RequestBody RemoveLocalJobTriggerDTO dto) {
        jobManageService.removeLocal(dto);
        return ApiResult.success();
    }
    
    @PostMapping("/trigger")
    @ApiOperation(value = "执行job", httpMethod = "POST")
    public ApiResult<String> triggerJob(@Valid @RequestBody JobManageDTO dto) {
        try {
            jobManageService.triggerJob(dto);
            return ApiResult.success();
        } catch (JobManageException e) {
            log.error(e.getMessage(), e);
            return ApiResult.failure(ApiStatus.SERVER_ERROR.status, e.getMessage());
        }
    }

    @PostMapping("/pause")
    @ApiOperation(value = "暂停job", httpMethod = "POST")
    public ApiResult<String> pauseJob(@Valid @RequestBody JobManageDTO dto) {
        try {
            jobManageService.pauseJob(dto);
            return ApiResult.success();
        } catch (JobManageException e) {
            log.error(e.getMessage(), e);
            return ApiResult.failure(ApiStatus.SERVER_ERROR.status, e.getMessage());
        }
    }

    @PostMapping("/resume")
    @ApiOperation(value = "恢复job", httpMethod = "POST")
    public ApiResult<String> resumeJob(@Valid @RequestBody JobManageDTO dto) {
        try {
            jobManageService.resumeJob(dto);
            return ApiResult.success();
        } catch (JobManageException e) {
            log.error(e.getMessage(), e);
            return ApiResult.failure(ApiStatus.SERVER_ERROR.status, e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除job", httpMethod = "POST")
    public ApiResult<String> deleteJob(@Valid @RequestBody JobManageDTO dto) {
        try {
            jobManageService.deleteJob(dto);
            return ApiResult.success();
        } catch (JobManageException e) {
            log.error(e.getMessage(), e);
            return ApiResult.failure(ApiStatus.SERVER_ERROR.status, e.getMessage());
        }
    }

    @PostMapping("/schedule")
    @ApiOperation(value = "修改job", httpMethod = "POST")
    public ApiResult<String> scheduleJob(@Valid @RequestBody ScheduleJobDto dto) {
        try {
            jobManageService.scheduleJob(dto);
            return ApiResult.success();
        } catch (JobManageException e) {
            log.error(e.getMessage(), e);
            return ApiResult.failure(ApiStatus.SERVER_ERROR.status, e.getMessage());
        }
    }

    @PostMapping("/execution-record/add")
    @ApiOperation(value = "保存任务执行记录", httpMethod = "POST")
    public ApiResult<String> saveJobExecutionRecord(@RequestBody @Valid JobExecutionRecordDto dto) {
        try {
            jobManageService.saveJobExecutionRecord(dto);
            return ApiResult.success();
        } catch (JobManageException e) {
            log.error(e.getMessage(), e);
            return ApiResult.failure(ApiStatus.SERVER_ERROR.status, e.getMessage());
        }
    }

    @GetMapping("/execution-record/list")
    @ApiOperation(value = "获取任务执行记录", httpMethod = "GET")
    public ApiResult<PageResponse<JobExecutionRecordDto>> getJobExecutionRecord(@Valid QueryJobTriggerInfo dto) {
        PageResponse<JobExecutionRecordDto> data = jobManageService.getJobExecutionRecord(dto);
        return ApiResult.success(data);
    }
}
