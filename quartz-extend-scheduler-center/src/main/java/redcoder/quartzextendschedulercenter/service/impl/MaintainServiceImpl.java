package redcoder.quartzextendschedulercenter.service.impl;

import lombok.extern.slf4j.Slf4j;
import redcoder.quartzextendcommon.utils.IpUtils;
import redcoder.quartzextendschedulercenter.mapper.QuartzSchedulerInstanceMapper;
import redcoder.quartzextendschedulercenter.mapper.QuartzSchedulerJobTriggerInfoMapper;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerInstance;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerJobTriggerInfo;
import redcoder.quartzextendschedulercenter.service.MaintainService;
import org.quartz.*;
import org.quartz.Trigger.TriggerState;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

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

    @Autowired
    private Scheduler scheduler;
    @Resource
    private QuartzSchedulerJobTriggerInfoMapper infoMapper;
    @Resource
    private QuartzSchedulerInstanceMapper instanceMapper;
    @Resource
    private Environment environment;

    @Override
    public void registerOwnInstance() throws SchedulerException {
        QuartzSchedulerInstance instance = new QuartzSchedulerInstance();
        instance.setSchedName(scheduler.getSchedulerName());
        instance.setInstanceHost(IpUtils.getLocalIp());
        instance.setInstancePort(Integer.valueOf(Objects.requireNonNull(environment.getProperty("server.port"))));
        try {
            instanceMapper.insertSelective(instance);
        } catch (DuplicateKeyException e) {
            // 重复数据，忽略
        }
    }

    @Override
    public void registerOwnJob() throws SchedulerException {
        String schedulerName = scheduler.getSchedulerName();

        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyTriggerGroup());
        for (TriggerKey triggerKey : triggerKeys) {
            QuartzSchedulerJobTriggerInfo jobTriggerInfo = createJobTriggerInfo(triggerKey);
            jobTriggerInfo.setSchedName(schedulerName);

            if (infoMapper.selectByPrimaryKey(jobTriggerInfo) != null) {
                // 更新数据
                jobTriggerInfo.setUpdateTime(new Date());
                infoMapper.updateByPrimaryKeySelective(jobTriggerInfo);
            } else {
                // 新增数据
                jobTriggerInfo.setCreateTime(new Date());
                jobTriggerInfo.setUpdateTime(new Date());
                infoMapper.insertSelective(jobTriggerInfo);
            }
        }
    }

    private QuartzSchedulerJobTriggerInfo createJobTriggerInfo(TriggerKey triggerKey) throws SchedulerException {
        QuartzSchedulerJobTriggerInfo jobTriggerInfo = new QuartzSchedulerJobTriggerInfo();

        Trigger trigger = scheduler.getTrigger(triggerKey);
        TriggerState triggerState = scheduler.getTriggerState(triggerKey);
        // 设置触发器相关属性
        jobTriggerInfo.setTriggerName(triggerKey.getName());
        jobTriggerInfo.setTriggerGroup(triggerKey.getGroup());
        jobTriggerInfo.setTriggerDesc(trigger.getDescription());
        Optional.ofNullable(trigger.getPreviousFireTime()).ifPresent(jobTriggerInfo::setPrevFireTime);
        Optional.ofNullable(trigger.getNextFireTime()).ifPresent(jobTriggerInfo::setNextFireTime);
        jobTriggerInfo.setTriggerState(triggerState.name());

        JobKey jobKey = trigger.getJobKey();
        if (jobKey != null) {
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            // 设置job相关属性
            jobTriggerInfo.setJobName(jobDetail.getKey().getName());
            jobTriggerInfo.setJobGroup(jobDetail.getKey().getGroup());
            jobTriggerInfo.setJobDesc(jobDetail.getDescription());
        }

        return jobTriggerInfo;
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
