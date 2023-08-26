package redcoder.quartzplus.schedcenter.service.impl;

import lombok.extern.slf4j.Slf4j;
import redcoder.quartzplus.schedcenter.dto.instance.QuartzInstanceDTO;
import redcoder.quartzplus.schedcenter.entity.QuartzSchedulerInstance;
import redcoder.quartzplus.schedcenter.entity.key.QuartzSchedulerInstanceKey;
import redcoder.quartzplus.schedcenter.repository.InstanceRepository;
import redcoder.quartzplus.schedcenter.repository.JobTriggerInfoRepository;
import redcoder.quartzplus.schedcenter.service.QuartzJobSchedulerService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
