package redcoder.quartzextendschedulercenter.service.impl;

import lombok.extern.slf4j.Slf4j;
import redcoder.quartzextendschedulercenter.dto.instance.QuartzInstanceDTO;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerInstance;
import redcoder.quartzextendschedulercenter.entity.key.QuartzSchedulerInstanceKey;
import redcoder.quartzextendschedulercenter.repository.InstanceRepository;
import redcoder.quartzextendschedulercenter.repository.JobTriggerInfoRepository;
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
    private JobTriggerInfoRepository jobTriggerInfoRepository;
    @Resource
    private InstanceRepository instanceRepository;

    @Override
    public boolean addInstance(QuartzInstanceDTO dto) {
        QuartzSchedulerInstanceKey key = new QuartzSchedulerInstanceKey();
        key.setSchedName(dto.getSchedName());
        key.setInstanceHost(dto.getInstanceHost());
        key.setInstancePort(dto.getInstancePort());
        if (instanceRepository.existsById(key)) {
            // 已存在
            return true;
        }
        // 新增
        QuartzSchedulerInstance instance = new QuartzSchedulerInstance();
        BeanUtils.copyProperties(dto, instance);
        instanceRepository.save(instance);
        return true;
    }

    @Override
    public boolean deleteInstance(QuartzInstanceDTO dto) {
        // 删除实例
        QuartzSchedulerInstanceKey key = new QuartzSchedulerInstanceKey();
        key.setSchedName(dto.getSchedName());
        key.setInstanceHost(dto.getInstanceHost());
        key.setInstancePort(dto.getInstancePort());
        instanceRepository.deleteById(key);

        // 删除此实例下的job数据
        jobTriggerInfoRepository.deleteBySchedName(dto.getSchedName());

        return true;
    }
}
