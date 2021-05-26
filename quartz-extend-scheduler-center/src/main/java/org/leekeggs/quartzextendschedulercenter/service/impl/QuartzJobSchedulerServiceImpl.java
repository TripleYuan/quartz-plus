package org.leekeggs.quartzextendschedulercenter.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.leekeggs.quartzextendcommon.utils.HttpTemplate;
import org.leekeggs.quartzextendcommon.utils.MapUtils;
import org.leekeggs.quartzextendschedulercenter.exception.RefreshJobTriggerInfoException;
import org.leekeggs.quartzextendschedulercenter.exception.TriggerJobException;
import org.leekeggs.quartzextendschedulercenter.mapper.QuartzSchedulerInstanceMapper;
import org.leekeggs.quartzextendschedulercenter.mapper.QuartzSchedulerJobTriggerInfoMapper;
import org.leekeggs.quartzextendschedulercenter.model.dto.ApiResult;
import org.leekeggs.quartzextendschedulercenter.model.dto.QuartzSchedulerInstanceDTO;
import org.leekeggs.quartzextendschedulercenter.model.dto.QuartzSchedulerJobTriggerInfoDTO;
import org.leekeggs.quartzextendschedulercenter.model.dto.tasklist.RefreshJobTriggerInfoDTO;
import org.leekeggs.quartzextendschedulercenter.model.dto.tasklist.TriggerJobDTO;
import org.leekeggs.quartzextendschedulercenter.model.entity.QuartzSchedulerInstance;
import org.leekeggs.quartzextendschedulercenter.model.entity.QuartzSchedulerJobTriggerInfo;
import org.leekeggs.quartzextendschedulercenter.service.QuartzJobSchedulerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.leekeggs.quartzextendschedulercenter.constant.QuartzApiConstants.QUARTZ_JOB_TRIGGER_INFO_QUERY;
import static org.leekeggs.quartzextendschedulercenter.constant.QuartzApiConstants.QUARTZ_TRIGGER_JOB;

/**
 * @author leekeggs
 * @since 2021-04-25
 */
@Service
@Slf4j
public class QuartzJobSchedulerServiceImpl implements QuartzJobSchedulerService {

    @Resource
    private QuartzSchedulerInstanceMapper instanceMapper;
    @Resource
    private QuartzSchedulerJobTriggerInfoMapper infoMapper;

    @Override
    public boolean addInstance(QuartzSchedulerInstanceDTO dto) {
        QuartzSchedulerInstance instance = new QuartzSchedulerInstance();
        BeanUtils.copyProperties(dto, instance);
        if (instanceMapper.selectByPrimaryKey(instance) != null) {
            // 已存在
            return true;
        }
        // 新增
        instanceMapper.insertSelective(instance);
        return true;
    }

    @Override
    public boolean deleteInstance(QuartzSchedulerInstanceDTO dto) {
        QuartzSchedulerInstance instance = new QuartzSchedulerInstance();
        BeanUtils.copyProperties(dto, instance);
        int i = instanceMapper.deleteByPrimaryKey(instance);
        return i > 0;
    }

    @Override
    public void refreshJobTriggerInfo(RefreshJobTriggerInfoDTO dto) {
        QuartzSchedulerInstance instance = getQuartzSchedulerInstance(dto.getSchedName());

        String url = "http://" + instance.getInstanceHost() + ":" + instance.getInstancePort() + QUARTZ_JOB_TRIGGER_INFO_QUERY;
        Map<String, String> queryParams = MapUtils.buildMap("triggerName", dto.getTriggerName(),
                "triggerGroup", dto.getTriggerGroup());
        ApiResult<QuartzSchedulerJobTriggerInfoDTO> result = HttpTemplate.doGet(url, queryParams,
                new TypeReference<ApiResult<QuartzSchedulerJobTriggerInfoDTO>>() {
                });
        if (result.getStatus() != 0) {
            throw new RefreshJobTriggerInfoException("刷新job和trigger信息失败：" + result.getMessage());
        }

        // 更新数据
        QuartzSchedulerJobTriggerInfoDTO data = result.getData();
        QuartzSchedulerJobTriggerInfo info = QuartzSchedulerJobTriggerInfo.valueOf(data);
        info.setUpdateTime(new Date());
        infoMapper.updateByPrimaryKeySelective(info);
    }

    @Override
    public void triggerJob(TriggerJobDTO dto) {
        QuartzSchedulerInstance instance = getQuartzSchedulerInstance(dto.getSchedName());
        String url = "http://" + instance.getInstanceHost() + ":" + instance.getInstancePort() + QUARTZ_TRIGGER_JOB;
        Map<String, String> formParams = MapUtils.buildMap("jobName", dto.getJobName(), "jobGroup", dto.getJobGroup());
        ApiResult<Boolean> result = HttpTemplate.doPost(url, formParams,
                new TypeReference<ApiResult<Boolean>>() {
                });
        if (result.getStatus() == 0 && Boolean.TRUE.equals(result.getData())) {
            return;
        }
        throw new TriggerJobException("触发job失败：" + result.getMessage());
    }

    private QuartzSchedulerInstance getQuartzSchedulerInstance(String schedName) {
        Example example = Example.builder(QuartzSchedulerInstance.class)
                .select("instanceHost", "instancePort")
                .where(Sqls.custom().andEqualTo("schedName", schedName))
                .build();
        List<QuartzSchedulerInstance> instances = instanceMapper.selectByExample(example);
        return instances.get(0);
    }
}
