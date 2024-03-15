package redcoder.quartzplus.schedcenter.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import redcoder.quartzplus.common.utils.IpUtils;
import redcoder.quartzplus.core.core.QuartzJobTriggerInfoCreator;
import redcoder.quartzplus.core.core.dto.QuartzJobInfo;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusInstance;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusJobInfo;
import redcoder.quartzplus.schedcenter.entity.key.QuartzPlusJobInfoKey;
import redcoder.quartzplus.schedcenter.repository.InstanceRepository;
import redcoder.quartzplus.schedcenter.repository.JobInfoRepository;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SchedulerCenterMaintainServiceImpl implements SchedulerCenterMaintainService, InitializingBean, EnvironmentAware {

    private Scheduler scheduler;
    private JobInfoRepository jobInfoRepository;
    private InstanceRepository instanceRepository;
    private QuartzJobTriggerInfoCreator creator;
    private Environment environment;

    @Override
    public void register() {
        try {
            registerOwnInstance();
            registerOwnJob();
        } catch (Exception e) {
            log.error("Failed to register!", e);
        }
    }

    /**
     * 注册调度中心服务应用本身的实例
     */
    private void registerOwnInstance() throws SchedulerException {
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

    /**
     * 注册调度中心服务应用本身的job
     */
    private void registerOwnJob() throws SchedulerException {
        String schedulerName = scheduler.getSchedulerName();

        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
        for (TriggerKey triggerKey : triggerKeys) {
            QuartzPlusJobInfoKey key =
                    new QuartzPlusJobInfoKey(schedulerName, triggerKey.getName(), triggerKey.getGroup());

            Optional<QuartzPlusJobInfo> optional = jobInfoRepository.findById(key);
            if (optional.isPresent()) {
                // 更新数据
                QuartzPlusJobInfo info = optional.get();
                info.setUpdateTime(new Date());
                jobInfoRepository.save(info);
            } else {
                // 新增数据
                QuartzJobInfo source = creator.create(triggerKey);
                QuartzPlusJobInfo target = QuartzPlusJobInfo.valueOf(source);
                jobInfoRepository.save(target);
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
        }, "SchedulerMaintainServiceThread").start();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Autowired
    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Autowired
    public void setJobTriggerInfoRepository(JobInfoRepository jobInfoRepository) {
        this.jobInfoRepository = jobInfoRepository;
    }

    @Autowired
    public void setInstanceRepository(InstanceRepository instanceRepository) {
        this.instanceRepository = instanceRepository;
    }

    @Autowired
    public void setCreator(QuartzJobTriggerInfoCreator creator) {
        this.creator = creator;
    }
}
