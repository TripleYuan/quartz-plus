package org.leekeggs.quartzextendschedulercenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.leekeggs.quartzextendschedulercenter.model.dto.ApiResult;
import org.leekeggs.quartzextendschedulercenter.service.MaintainService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leekeggs
 * @since 2021-04-27
 */
@RestController
@RequestMapping("/api/maintain")
@Api(tags = "Maintain API")
@Slf4j
public class MaintainController {

    @Autowired
    private MaintainService maintainService;

    @PostMapping("/register-own-instance")
    @ApiOperation(value = "注册调度器应用本身的实例", httpMethod = "POST")
    public ApiResult<Boolean> registerOwnInstance() {
        try {
            maintainService.registerOwnInstance();
            return ApiResult.success(true);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            ApiResult<Boolean> result = ApiResult.success(false);
            result.setMessage(e.getMessage());
            return result;
        }
    }

    @PostMapping("/register-own-job")
    @ApiOperation(value = "注册调度器应用本身的job", httpMethod = "POST")
    public ApiResult<Boolean> registerOwnJob() {
        try {
            maintainService.registerOwnJob();
            return ApiResult.success(true);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            ApiResult<Boolean> result = ApiResult.success(false);
            result.setMessage(e.getMessage());
            return result;
        }
    }
}
