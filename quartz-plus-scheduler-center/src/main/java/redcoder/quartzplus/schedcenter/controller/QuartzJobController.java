package redcoder.quartzplus.schedcenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import redcoder.quartzplus.schedcenter.constant.ApiStatus;
import redcoder.quartzplus.schedcenter.dto.job.*;
import redcoder.quartzplus.schedcenter.exception.JobManageException;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.PageResponse;
import redcoder.quartzplus.schedcenter.service.QuartzJobService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "Quartz任务管理")
@Slf4j
public class QuartzJobController {

    private QuartzJobService jobService;

    public QuartzJobController(QuartzJobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/api/job/sched-names")
    @ApiOperation(value = "获取quartz实例名称", httpMethod = "GET")
    public ApiResult<List<String>> getSchedNames() {
        return ApiResult.success(jobService.getSchedNames());
    }

    @GetMapping("/api/jobs")
    @ApiOperation(value = "获取job", httpMethod = "GET")
    public ApiResult<PageResponse<JobInfo>> getJobTriggerInfoList(JobInfoQuery jobInfoQuery) {
        return ApiResult.success(jobService.getJobInfo(jobInfoQuery));
    }

    @PostMapping("/api/job")
    @ApiOperation(value = "修改job", httpMethod = "POST")
    public ApiResult<String> scheduleJob(@Valid @RequestBody JobInfoUpdate jobInfoUpdate) {
        try {
            jobService.updateJob(jobInfoUpdate);
            return ApiResult.success();
        } catch (JobManageException e) {
            log.error(e.getMessage(), e);
            return ApiResult.failure(ApiStatus.SERVER_ERROR.status, e.getMessage());
        }
    }

    @DeleteMapping("/api/job/{schedName}/{jobGroup}/{jobName}")
    @ApiOperation(value = "删除job")
    public ApiResult<String> deleteJob(@PathVariable String schedName,
                                       @PathVariable String jobName,
                                       @PathVariable String jobGroup) {
        try {
            jobService.deleteJob(new JobUniqueId(schedName, jobName, jobGroup));
            return ApiResult.success();
        } catch (JobManageException e) {
            log.error(e.getMessage(), e);
            return ApiResult.failure(ApiStatus.SERVER_ERROR.status, e.getMessage());
        }
    }

    @DeleteMapping("/api/local-job/{schedName}/{jobGroup}/{jobName}")
    @ApiOperation(value = "删除本地保存的job数据")
    public ApiResult<String> removeLocal(@PathVariable String schedName,
                                         @PathVariable String jobName,
                                         @PathVariable String jobGroup) {
        jobService.removeLocal(new JobUniqueId(schedName, jobName, jobGroup));
        return ApiResult.success();
    }

    @PostMapping("/api/job/refresh")
    @ApiOperation(value = "刷新job信息", httpMethod = "POST")
    public ApiResult<JobInfo> refreshJob(@Valid @RequestBody JobInfoRefresh jobInfoRefresh) {
        return ApiResult.success(jobService.refreshJob(jobInfoRefresh));
    }

    @PostMapping("/api/job/execute")
    @ApiOperation(value = "执行job", httpMethod = "POST")
    public ApiResult<String> executeJob(@Valid @RequestBody JobUniqueId jobUniqueId) {
        try {
            jobService.executeJob(jobUniqueId);
            return ApiResult.success();
        } catch (JobManageException e) {
            log.error(e.getMessage(), e);
            return ApiResult.failure(ApiStatus.SERVER_ERROR.status, e.getMessage());
        }
    }

    @PostMapping("/api/job/pause")
    @ApiOperation(value = "暂停job", httpMethod = "POST")
    public ApiResult<String> pauseJob(@Valid @RequestBody JobUniqueId jobUniqueId) {
        try {
            jobService.pauseJob(jobUniqueId);
            return ApiResult.success();
        } catch (JobManageException e) {
            log.error(e.getMessage(), e);
            return ApiResult.failure(ApiStatus.SERVER_ERROR.status, e.getMessage());
        }
    }

    @PostMapping("/api/job/resume")
    @ApiOperation(value = "恢复job", httpMethod = "POST")
    public ApiResult<String> resumeJob(@Valid @RequestBody JobUniqueId jobUniqueId) {
        try {
            jobService.resumeJob(jobUniqueId);
            return ApiResult.success();
        } catch (JobManageException e) {
            log.error(e.getMessage(), e);
            return ApiResult.failure(ApiStatus.SERVER_ERROR.status, e.getMessage());
        }
    }

    @PostMapping("/api/job/execution-record/add")
    @ApiOperation(value = "保存任务执行记录", httpMethod = "POST")
    public ApiResult<String> saveJobExecutionRecord(@RequestBody @Valid JobExecutionRecord record) {
        try {
            jobService.saveJobExecutionRecord(record);
            return ApiResult.success();
        } catch (JobManageException e) {
            log.error(e.getMessage(), e);
            return ApiResult.failure(ApiStatus.SERVER_ERROR.status, e.getMessage());
        }
    }

    @GetMapping("/api/job/execution-records")
    @ApiOperation(value = "获取任务执行记录", httpMethod = "GET")
    public ApiResult<PageResponse<JobExecutionRecord>> getJobExecutionRecord(@Valid JobInfoQuery dto) {
        PageResponse<JobExecutionRecord> data = jobService.getJobExecutionRecord(dto);
        return ApiResult.success(data);
    }
}
