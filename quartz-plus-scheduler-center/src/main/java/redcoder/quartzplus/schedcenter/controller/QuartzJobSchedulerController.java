package redcoder.quartzplus.schedcenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.instance.QuartzInstanceDTO;
import redcoder.quartzplus.schedcenter.service.QuartzJobSchedulerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author redcoder54
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
    public ApiResult<Boolean> addInstance(@Valid @RequestBody QuartzInstanceDTO dto) {
        return ApiResult.success(service.addInstance(dto));
    }

    @PostMapping("/instance/unregister")
    @ApiOperation(value = "取消注册的实例信息", httpMethod = "POST")
    public ApiResult<Boolean> deleteInstance(@Valid @RequestBody QuartzInstanceDTO dto) {
        return ApiResult.success(service.deleteInstance(dto));
    }
}
