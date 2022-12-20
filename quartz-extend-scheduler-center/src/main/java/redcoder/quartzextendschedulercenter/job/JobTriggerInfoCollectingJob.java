package redcoder.quartzextendschedulercenter.job;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import redcoder.quartzextendcommon.utils.HttpTemplate;
import redcoder.quartzextendcommon.utils.JsonUtils;
import redcoder.quartzextendcore.annotation.QuartzJob;
import redcoder.quartzextendcore.annotation.QuartzTrigger;
import redcoder.quartzextendcore.core.dto.QuartzJobTriggerInfo;
import redcoder.quartzextendschedulercenter.mapper.QuartzSchedulerInstanceMapper;
import redcoder.quartzextendschedulercenter.mapper.QuartzSchedulerJobTriggerInfoMapper;
import redcoder.quartzextendschedulercenter.dto.ApiResult;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerInstance;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerJobTriggerInfo;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.*;

import static redcoder.quartzextendschedulercenter.constant.QuartzApiConstants.JOB_TRIGGER_INFO_LIST;

/**
 * 采集quartz job和trigger信息，保存到数据库表中
 *
 * @author redcoder54
 * @since 2021-04-25
 */
@Slf4j
@QuartzJob(jobDescription = "采集quartz实例上的job和trigger信息")
@QuartzTrigger(triggerDescription = "JobTriggerInfoCollectingJob's trigger", cron = "0 0/5 * * * ?")
public class JobTriggerInfoCollectingJob extends QuartzJobBean {

    @Resource
    private QuartzSchedulerInstanceMapper instanceMapper;
    @Resource
    private QuartzSchedulerJobTriggerInfoMapper infoMapper;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        try {
            Set<String> handledScheduler = new HashSet<>();
            List<QuartzSchedulerInstance> instances = instanceMapper.selectAll();
            for (QuartzSchedulerInstance instance : instances) {
                String schedName = instance.getSchedName();
                if (!handledScheduler.contains(schedName)) {
                    try {
                        List<QuartzJobTriggerInfo> jobTriggerInfos = getJobTriggerInfos(instance);
                        clearThenInsert(schedName, jobTriggerInfos);
                        handledScheduler.add(schedName);
                    } catch (Exception e) {
                        log.error("收集数据失败: " + JsonUtils.beanToJsonString(instance), e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("JobTriggerInfoCollectingJob error", e);
        }
    }

    private List<QuartzJobTriggerInfo> getJobTriggerInfos(QuartzSchedulerInstance instance) {
        String url = "http://" + instance.getInstanceHost() + ":" + instance.getInstancePort() + JOB_TRIGGER_INFO_LIST;
        ApiResult<List<QuartzJobTriggerInfo>> result = HttpTemplate.doGet(url, new TypeReference<ApiResult<List<QuartzJobTriggerInfo>>>() {
        });
        if (result.getStatus() != 0) {
            log.error("获取QuartzJobTriggerInfo失败，原因：{}", result.getMessage());
            return Collections.emptyList();
        }
        return result.getData();
    }

    /**
     * 删除指定的schedName数据，然后再插入
     *
     * @param jobTriggerInfos job和trigger数据
     */
    private void clearThenInsert(String schedName, List<QuartzJobTriggerInfo> jobTriggerInfos) {
        if (jobTriggerInfos.isEmpty()) {
            return;
        }
        // delete
        QuartzSchedulerJobTriggerInfo deleteR = new QuartzSchedulerJobTriggerInfo();
        deleteR.setSchedName(schedName);
        infoMapper.delete(deleteR);
        // insert
        for (QuartzJobTriggerInfo dto : jobTriggerInfos) {
            QuartzSchedulerJobTriggerInfo info = QuartzSchedulerJobTriggerInfo.valueOf(dto);
            info.setCreateTime(new Date());
            info.setUpdateTime(new Date());
            infoMapper.insertSelective(info);
        }
    }
}
