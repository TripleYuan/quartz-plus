package org.leekeggs.quartzextendschedulercenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.leekeggs.quartzextendschedulercenter.model.dto.ApiResult;
import org.leekeggs.quartzextendschedulercenter.model.dto.QuartzSchedulerInstanceDTO;
import org.leekeggs.quartzextendschedulercenter.service.QuartzJobSchedulerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author leekeggs
 * @since 2021-04-25
 */
@RestController
@RequestMapping("/api/quartz-job-scheduler")
@Api(tags = "QuartzJobScheduler API")
public class QuartzJobSchedulerController {

    private QuartzJobSchedulerService service;

    public QuartzJobSchedulerController(QuartzJobSchedulerService service) {
        this.service = service;
    }

    @PostMapping("/instance/register")
    @ApiOperation(value = "注册实例信息", httpMethod = "POST")
    public ApiResult<Boolean> addInstance(@Valid @RequestBody QuartzSchedulerInstanceDTO dto) {
        return ApiResult.success(service.addInstance(dto));
    }
}
