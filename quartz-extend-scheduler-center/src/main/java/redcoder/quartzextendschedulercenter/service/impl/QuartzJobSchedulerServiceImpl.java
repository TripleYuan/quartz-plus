package redcoder.quartzextendschedulercenter.service.impl;

import lombok.extern.slf4j.Slf4j;
import redcoder.quartzextendschedulercenter.mapper.QuartzSchedulerInstanceMapper;
import redcoder.quartzextendschedulercenter.mapper.QuartzSchedulerJobTriggerInfoMapper;
import redcoder.quartzextendschedulercenter.dto.instance.QuartzInstanceDTO;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerInstance;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerJobTriggerInfo;
import redcoder.quartzextendschedulercenter.service.QuartzJobSchedulerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author redcoder54
 * @since 2021-04-25
 */
@Service
@Slf4j
public class QuartzJobSchedulerServiceImpl implements QuartzJobSchedulerService {

    @Resource
    private QuartzSchedulerInstanceMapper instanceMapper;
    @Resource
    private QuartzSchedulerJobTriggerInfoMapper jobTriggerInfoMapper;

    @Override
    public boolean addInstance(QuartzInstanceDTO dto) {
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
    public boolean deleteInstance(QuartzInstanceDTO dto) {
        // 删除实例
        QuartzSchedulerInstance instance = new QuartzSchedulerInstance();
        BeanUtils.copyProperties(dto, instance);
        instanceMapper.deleteByPrimaryKey(instance);

        // 删除此实例下的job数据
        QuartzSchedulerJobTriggerInfo jobTriggerInfo = new QuartzSchedulerJobTriggerInfo();
        jobTriggerInfo.setSchedName(dto.getSchedName());
        jobTriggerInfoMapper.delete(jobTriggerInfo);

        return true;
    }
}
