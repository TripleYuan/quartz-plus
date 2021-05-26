package org.leekeggs.quartzextendschedulercenter.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.leekeggs.quartzextendschedulercenter.mapper.QuartzSchedulerInstanceMapper;
import org.leekeggs.quartzextendschedulercenter.model.dto.ApiResult;
import org.leekeggs.quartzextendschedulercenter.model.dto.QuartzSchedulerInstanceDTO;
import org.leekeggs.quartzextendschedulercenter.model.dto.instancelist.InstanceDTO;
import org.leekeggs.quartzextendschedulercenter.model.entity.QuartzSchedulerInstance;
import org.leekeggs.quartzextendschedulercenter.service.QuartzJobSchedulerService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author leekeggs
 * @since 2021-04-26
 */
@RestController
@RequestMapping("/api/instance")
@Api(tags = "实例列表")
public class InstanceListController {

    @Resource
    private QuartzSchedulerInstanceMapper instanceMapper;
    @Resource
    private QuartzJobSchedulerService quartzJobSchedulerService;

    @GetMapping("/list")
    @ApiOperation(value = "获取实例列表", httpMethod = "GET")
    public ApiResult<List<InstanceDTO>> getInstancesList() {
        List<QuartzSchedulerInstance> list = instanceMapper.selectAll();
        List<InstanceDTO> data = list.stream().map(InstanceDTO::valueOf).collect(Collectors.toList());
        return ApiResult.success(data);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "删除实例列表", httpMethod = "DELETE")
    public ApiResult<Boolean> delete(@Valid @RequestBody InstanceDTO dto) {
        QuartzSchedulerInstanceDTO quartzSchedulerInstanceDTO = new QuartzSchedulerInstanceDTO();
        BeanUtils.copyProperties(dto, quartzSchedulerInstanceDTO);
        return ApiResult.success(quartzJobSchedulerService.deleteInstance(quartzSchedulerInstanceDTO));
    }
}
