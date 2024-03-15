package redcoder.quartzplus.schedcenter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.PageResponse;
import redcoder.quartzplus.schedcenter.dto.instance.QuartzInstanceInfo;
import redcoder.quartzplus.schedcenter.dto.instance.QueryInstanceInfo;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusInstance;
import redcoder.quartzplus.schedcenter.repository.InstanceRepository;
import redcoder.quartzplus.schedcenter.service.QuartzSchedulerInstanceService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "Quartz实例管理")
public class QuartzInstanceController {

    private InstanceRepository instanceRepository;
    private QuartzSchedulerInstanceService instanceService;

    @Autowired
    public QuartzInstanceController(InstanceRepository instanceRepository,
                                    QuartzSchedulerInstanceService instanceService) {
        this.instanceRepository = instanceRepository;
        this.instanceService = instanceService;
    }

    @GetMapping("/api/instances")
    @ApiOperation(value = "获取实例", httpMethod = "GET")
    public ApiResult<PageResponse<QuartzInstanceInfo>> getInstancesList(QueryInstanceInfo queryInstanceInfo) {
        String schedName = queryInstanceInfo.getSchedName();
        int pageNo = queryInstanceInfo.getPageNo() - 1;
        int pageSize = queryInstanceInfo.getPageSize();

        Page<QuartzPlusInstance> page;
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("schedName"));
        if (StringUtils.hasText(schedName)) {
            page = instanceRepository.findBySchedName(schedName, pageRequest);
        } else {
            page = instanceRepository.findAll(pageRequest);
        }
        List<QuartzInstanceInfo> data = page.stream().map(QuartzInstanceInfo::valueOf).collect(Collectors.toList());
        return ApiResult.success(new PageResponse<>(page.getTotalElements(), pageNo, pageSize, data));
    }

    @DeleteMapping("/api/instance/{schedName}/{host}/{port}")
    @ApiOperation(value = "删除实例")
    public ApiResult<Boolean> delete(@PathVariable String schedName,
                                     @PathVariable String host,
                                     @PathVariable Integer port) {
        return ApiResult.success(instanceService.removeInstance(new QuartzInstanceInfo(schedName, host, port)));
    }
}
