package org.leekeggs.quartzextendschedulercenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.leekeggs.quartzextendschedulercenter.exception.JobManageException;
import org.leekeggs.quartzextendschedulercenter.model.dto.ApiResult;
import org.leekeggs.quartzextendschedulercenter.model.dto.job.JobManageDTO;
import org.leekeggs.quartzextendschedulercenter.model.dto.job.JobTriggerDTO;
import org.leekeggs.quartzextendschedulercenter.model.dto.job.RefreshJobTriggerDTO;
import org.leekeggs.quartzextendschedulercenter.model.dto.job.RemoveLocalJobTriggerDTO;
import org.leekeggs.quartzextendschedulercenter.service.QuartzJobManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author redcoder54
 * @since 2022-01-09
 */
@RestController
@RequestMapping("/api/job-manage")
@Api(tags = "任务管理")
@Slf4j
public class QuartzJobManageController {

    @Autowired
    private QuartzJobManageService jobManageService;

    @GetMapping("/sched-names")
    @ApiOperation(value = "获取quartz实例名称", httpMethod = "GET")
    public ApiResult<List<String>> getSchedNames() {
        return ApiResult.success(jobManageService.getSchedNames());
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取job", httpMethod = "GET")
    public ApiResult<List<JobTriggerDTO>> getJobTriggerInfoList(@RequestParam(required = false) String schedName) {
        return ApiResult.success(jobManageService.getJobTriggerInfos(schedName));
    }

    @PostMapping("/refresh")
    @ApiOperation(value = "刷新job信息", httpMethod = "POST")
    public ApiResult<JobTriggerDTO> refreshJobTrigger(@Valid @RequestBody RefreshJobTriggerDTO dto) {
        return ApiResult.success(jobManageService.refreshJobTrigger(dto));
    }

    @DeleteMapping("/removeLocal")
    @ApiOperation(value = "删除本地保存的job数据", httpMethod = "DELETE")
    public ApiResult<Boolean> removeLocal(@Valid @RequestBody RemoveLocalJobTriggerDTO dto) {
        return ApiResult.success(jobManageService.removeLocal(dto));
    }

    @PostMapping("/trigger")
    @ApiOperation(value = "执行job", httpMethod = "POST")
    public ApiResult<Boolean> triggerJob(@Valid @RequestBody JobManageDTO dto) {
        try {
            jobManageService.triggerJob(dto);
            return ApiResult.success(true);
        } catch (JobManageException e) {
            log.error(e.getMessage(), e);
            ApiResult<Boolean> result = ApiResult.success(false);
            result.setMessage(e.getMessage());
            return result;
        }
    }

    @PostMapping("/pause")
    @ApiOperation(value = "暂停job", httpMethod = "POST")
    public ApiResult<Boolean> pauseJob(@Valid @RequestBody JobManageDTO dto) {
        try {
            jobManageService.pauseJob(dto);
            return ApiResult.success(true);
        } catch (JobManageException e) {
            log.error(e.getMessage(), e);
            ApiResult<Boolean> result = ApiResult.success(false);
            result.setMessage(e.getMessage());
            return result;
        }
    }

    @PostMapping("/resume")
    @ApiOperation(value = "恢复job", httpMethod = "POST")
    public ApiResult<Boolean> resumeJob(@Valid @RequestBody JobManageDTO dto) {
        try {
            jobManageService.resumeJob(dto);
            return ApiResult.success(true);
        } catch (JobManageException e) {
            log.error(e.getMessage(), e);
            ApiResult<Boolean> result = ApiResult.success(false);
            result.setMessage(e.getMessage());
            return result;
        }
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除job", httpMethod = "POST")
    public ApiResult<Boolean> deleteJob(@Valid @RequestBody JobManageDTO dto) {
        try {
            jobManageService.deleteJob(dto);
            return ApiResult.success(true);
        } catch (JobManageException e) {
            log.error(e.getMessage(), e);
            ApiResult<Boolean> result = ApiResult.success(false);
            result.setMessage(e.getMessage());
            return result;
        }
    }
}
