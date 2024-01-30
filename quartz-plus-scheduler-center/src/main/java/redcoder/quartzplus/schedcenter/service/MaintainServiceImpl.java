package redcoder.quartzplus.schedcenter.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import redcoder.quartzplus.common.utils.IpUtils;
import redcoder.quartzplus.core.core.QuartzJobTriggerInfoCreator;
import redcoder.quartzplus.core.core.dto.QuartzJobTriggerInfo;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusInstance;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusJobTriggerInfo;
import redcoder.quartzplus.schedcenter.entity.key.QuartzPlusJobTriggerInfoKey;
import redcoder.quartzplus.schedcenter.repository.InstanceRepository;
import redcoder.quartzplus.schedcenter.repository.JobTriggerInfoRepository;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MaintainServiceImpl implements MaintainService, InitializingBean {

    @Resource
    private Scheduler scheduler;
    @Resource
    private JobTriggerInfoRepository jobTriggerInfoRepository;
    @Resource
    private InstanceRepository instanceRepository;
    @Resource
    private Environment environment;
    @Resource
    private QuartzJobTriggerInfoCreator creator;

    @Override
    public void registerOwnInstance() throws SchedulerException {
        QuartzPlusInstance instance = new QuartzPlusInstance();
        instance.setSchedName(scheduler.getSchedulerName());
        instance.setInstanceHost(IpUtils.getLocalIp());
        instance.setInstancePort(Integer.valueOf(Objects.requireNonNull(environment.getProperty("server.port"))));
        try {
            instanceRepository.save(instance);
        } catch (DuplicateKeyException e) {
            // 重复数据，忽略
        }
    }

    @Override
    public void registerOwnJob() throws SchedulerException {
        String schedulerName = scheduler.getSchedulerName();

        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
        for (TriggerKey triggerKey : triggerKeys) {
            QuartzPlusJobTriggerInfoKey key =
                    new QuartzPlusJobTriggerInfoKey(schedulerName,triggerKey.getName(),triggerKey.getGroup());

            Optional<QuartzPlusJobTriggerInfo> optional = jobTriggerInfoRepository.findById(key);
            if (optional.isPresent()) {
                // 更新数据
                QuartzPlusJobTriggerInfo info = optional.get();
                info.setUpdateTime(new Date());
                jobTriggerInfoRepository.save(info);
            } else {
                // 新增数据
                QuartzJobTriggerInfo source = creator.create(triggerKey);
                QuartzPlusJobTriggerInfo target = QuartzPlusJobTriggerInfo.valueOf(source);
                jobTriggerInfoRepository.save(target);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                registerOwnInstance();
                registerOwnJob();
            } catch (Exception e) {
                log.error("", e);
            }
        }, "Self-Register-Thread").start();
    }
}
