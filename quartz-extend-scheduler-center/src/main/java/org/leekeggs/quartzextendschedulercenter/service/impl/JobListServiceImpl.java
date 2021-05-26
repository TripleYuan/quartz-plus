package org.leekeggs.quartzextendschedulercenter.service.impl;

import org.leekeggs.quartzextendschedulercenter.mapper.QuartzSchedulerJobTriggerInfoMapper;
import org.leekeggs.quartzextendschedulercenter.model.dto.tasklist.DeleteJobTriggerInfoDTO;
import org.leekeggs.quartzextendschedulercenter.model.dto.tasklist.JobTriggerInfoDTO;
import org.leekeggs.quartzextendschedulercenter.model.dto.tasklist.RefreshJobTriggerInfoDTO;
import org.leekeggs.quartzextendschedulercenter.model.dto.tasklist.TriggerJobDTO;
import org.leekeggs.quartzextendschedulercenter.model.entity.QuartzSchedulerJobTriggerInfo;
import org.leekeggs.quartzextendschedulercenter.service.JobListService;
import org.leekeggs.quartzextendschedulercenter.service.QuartzJobSchedulerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author leekeggs
 * @since 2021-04-25
 */
@Service
public class JobListServiceImpl implements JobListService {

    @Resource
    private QuartzSchedulerJobTriggerInfoMapper infoMapper;
    @Resource
    private QuartzJobSchedulerService schedulerService;

    @Override
    public List<JobTriggerInfoDTO> getJobTriggerInfoList() {
        Example example = Example.builder(QuartzSchedulerJobTriggerInfo.class)
                .orderByDesc("schedName")
                .build();
        List<QuartzSchedulerJobTriggerInfo> list = infoMapper.selectByExample(example);
        return list.stream().map(t -> {
            JobTriggerInfoDTO dto = new JobTriggerInfoDTO();
            // 属性赋值
            BeanUtils.copyProperties(t, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public JobTriggerInfoDTO refreshJobTriggerInfo(RefreshJobTriggerInfoDTO dto) {
        // 刷新数据
        schedulerService.refreshJobTriggerInfo(dto);
        // 获取刷新后的数据
        Example example = Example.builder(QuartzSchedulerJobTriggerInfo.class)
                .andWhere(Sqls.custom()
                        .andEqualTo("schedName", dto.getSchedName())
                        .andEqualTo("triggerName", dto.getTriggerName())
                        .andEqualTo("triggerGroup", dto.getTriggerGroup()))
                .build();
        QuartzSchedulerJobTriggerInfo jobTriggerInfo = infoMapper.selectOneByExample(example);

        JobTriggerInfoDTO jobTriggerInfoDTO = new JobTriggerInfoDTO();
        BeanUtils.copyProperties(jobTriggerInfo, jobTriggerInfoDTO);
        return jobTriggerInfoDTO;
    }

    @Override
    public boolean delJobTriggerInfo(DeleteJobTriggerInfoDTO dto) {
        QuartzSchedulerJobTriggerInfo info = new QuartzSchedulerJobTriggerInfo();
        info.setSchedName(dto.getSchedName());
        info.setTriggerName(dto.getTriggerName());
        info.setTriggerGroup(dto.getTriggerGroup());
        int i = infoMapper.deleteByPrimaryKey(info);
        return i > 0;
    }

    @Override
    public void triggerJob(TriggerJobDTO dto) {
        schedulerService.triggerJob(dto);
    }
}
