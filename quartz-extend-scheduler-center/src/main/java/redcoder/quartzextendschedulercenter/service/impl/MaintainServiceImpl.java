package redcoder.quartzextendschedulercenter.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.Trigger.TriggerState;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import redcoder.quartzextendcommon.utils.IpUtils;
import redcoder.quartzextendcore.core.QuartzJobTriggerInfoCreator;
import redcoder.quartzextendcore.core.dto.QuartzJobTriggerInfo;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerInstance;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerJobTriggerInfo;
import redcoder.quartzextendschedulercenter.entity.key.QuartzSchedulerInstanceKey;
import redcoder.quartzextendschedulercenter.entity.key.QuartzSchedulerJobTriggerInfoKey;
import redcoder.quartzextendschedulercenter.repository.InstanceRepository;
import redcoder.quartzextendschedulercenter.repository.JobTriggerInfoRepository;
import redcoder.quartzextendschedulercenter.service.MaintainService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author redcoder54
 * @since 2021-04-27
 */
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
        QuartzSchedulerInstance instance = new QuartzSchedulerInstance();
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
            QuartzSchedulerJobTriggerInfoKey key =
                    new QuartzSchedulerJobTriggerInfoKey(schedulerName,triggerKey.getName(),triggerKey.getGroup());

            Optional<QuartzSchedulerJobTriggerInfo> optional = jobTriggerInfoRepository.findById(key);
            if (optional.isPresent()) {
                // 更新数据
                QuartzSchedulerJobTriggerInfo info = optional.get();
                info.setUpdateTime(new Date());
                jobTriggerInfoRepository.save(info);
            } else {
                // 新增数据
                QuartzJobTriggerInfo source = creator.create(triggerKey);
                QuartzSchedulerJobTriggerInfo target = QuartzSchedulerJobTriggerInfo.valueOf(source);
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
