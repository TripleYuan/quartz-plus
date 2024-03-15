package redcoder.quartzplus.schedcenter.service;

import lombok.extern.slf4j.Slf4j;
import redcoder.quartzplus.schedcenter.collect.JobTriggerInfoCollector;
import redcoder.quartzplus.schedcenter.dto.instance.QuartzInstanceInfo;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusInstance;
import redcoder.quartzplus.schedcenter.entity.key.QuartzPlusInstanceKey;
import redcoder.quartzplus.schedcenter.repository.InstanceRepository;
import redcoder.quartzplus.schedcenter.repository.JobInfoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class QuartzSchedulerInstanceServiceImpl implements QuartzSchedulerInstanceService {

    private JobInfoRepository jobInfoRepository;
    private InstanceRepository instanceRepository;
    private JobTriggerInfoCollector collector;
    private ScheduledExecutorService executorService;

    public QuartzSchedulerInstanceServiceImpl(JobInfoRepository jobInfoRepository,
                                              InstanceRepository instanceRepository,
                                              JobTriggerInfoCollector collector) {
        this.jobInfoRepository = jobInfoRepository;
        this.instanceRepository = instanceRepository;
        this.collector = collector;
        this.executorService = Executors.newScheduledThreadPool(2);
    }

    @Override
    public boolean addInstance(QuartzInstanceInfo dto) {
        QuartzPlusInstanceKey key = new QuartzPlusInstanceKey();
        key.setSchedName(dto.getSchedName());
        key.setInstanceHost(dto.getInstanceHost());
        key.setInstancePort(dto.getInstancePort());
        if (instanceRepository.existsById(key)) {
            // 已存在
            return true;
        }

        // 新增
        QuartzPlusInstance instance = new QuartzPlusInstance();
        BeanUtils.copyProperties(dto, instance);
        instanceRepository.save(instance);

        executorService.schedule(() -> collector.collect(dto.getSchedName(), dto.getInstanceHost(), dto.getInstancePort()), 10, TimeUnit.SECONDS);

        return true;
    }

    @Override
    public boolean removeInstance(QuartzInstanceInfo dto) {
        // 删除实例
        QuartzPlusInstanceKey key = new QuartzPlusInstanceKey();
        key.setSchedName(dto.getSchedName());
        key.setInstanceHost(dto.getInstanceHost());
        key.setInstancePort(dto.getInstancePort());
        instanceRepository.deleteById(key);

        // 删除此实例下的job数据
        jobInfoRepository.deleteBySchedName(dto.getSchedName());

        return true;
    }
}
