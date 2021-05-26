package org.leekeggs.quartzextendschedulercenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.leekeggs.quartzextendschedulercenter.exception.TriggerJobException;
import org.leekeggs.quartzextendschedulercenter.model.dto.ApiResult;
import org.leekeggs.quartzextendschedulercenter.model.dto.tasklist.DeleteJobTriggerInfoDTO;
import org.leekeggs.quartzextendschedulercenter.model.dto.tasklist.JobTriggerInfoDTO;
import org.leekeggs.quartzextendschedulercenter.model.dto.tasklist.RefreshJobTriggerInfoDTO;
import org.leekeggs.quartzextendschedulercenter.model.dto.tasklist.TriggerJobDTO;
import org.leekeggs.quartzextendschedulercenter.service.JobListService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author leekeggs
 * @since 2021-04-25
 */
@RestController
@RequestMapping("/api/job")
@Api(tags = "任务列表")
@Slf4j
public class JobListController {

    private JobListService jobListService;

    public JobListController(JobListService jobListService) {
        this.jobListService = jobListService;
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取job列表", httpMethod = "GET")
    public ApiResult<List<JobTriggerInfoDTO>> getJobTriggerInfoList() {
        return ApiResult.success(jobListService.getJobTriggerInfoList());
    }

    @PostMapping("/refresh")
    @ApiOperation(value = "刷新job信息", httpMethod = "POST")
    public ApiResult<JobTriggerInfoDTO> refreshJobTriggerInfo(@Valid @RequestBody RefreshJobTriggerInfoDTO dto) {
        return ApiResult.success(jobListService.refreshJobTriggerInfo(dto));
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除job信息", httpMethod = "DELETE")
    public ApiResult<Boolean> deleteJobTriggerInfo(@Valid @RequestBody DeleteJobTriggerInfoDTO dto) {
        return ApiResult.success(jobListService.delJobTriggerInfo(dto));
    }

    @PostMapping("/trigger")
    @ApiOperation(value = "执行job", httpMethod = "POST")
    public ApiResult<Boolean> triggerJob(@Valid @RequestBody TriggerJobDTO dto) {
        try {
            jobListService.triggerJob(dto);
            return ApiResult.success(true);
        } catch (TriggerJobException e) {
            log.error(e.getMessage(), e);
            ApiResult<Boolean> result = ApiResult.success(false);
            result.setMessage(e.getMessage());
            return result;
        }
    }
}
