package redcoder.quartzextendschedulercenter.collecting;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redcoder.quartzextendcommon.utils.HttpTemplate;
import redcoder.quartzextendcore.core.dto.QuartzJobTriggerInfo;
import redcoder.quartzextendschedulercenter.dto.ApiResult;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerInstance;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerJobTriggerInfo;
import redcoder.quartzextendschedulercenter.repository.InstanceRepository;
import redcoder.quartzextendschedulercenter.repository.JobTriggerInfoRepository;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static redcoder.quartzextendschedulercenter.constant.QuartzApiConstants.JOB_TRIGGER_INFO_LIST;


/**
 * 负责采集job和trigger信息的采集器，对于集群模式的quartz实例，随机选择其中一个实例，进行信息采集。
 *
 * @author redcoder54
 */
@Slf4j
@Component
public class DefaultJobTriggerInfoCollector implements JobTriggerInfoCollector {

    @Resource
    private JobTriggerInfoRepository jobTriggerInfoRepository;
    @Resource
    private InstanceRepository instanceRepository;
    @Resource
    private CollectingErrorHandlerChain errorHandlerChain;

    @Override
    public void collecting() {
        Set<String> handledScheduler = new HashSet<>();
        instanceRepository.findAll().forEach(instance -> {
            String schedName = instance.getSchedName();
            if (!handledScheduler.contains(schedName)) {
                try {
                    List<QuartzJobTriggerInfo> jobTriggerInfos = getJobTriggerInfos(instance);
                    clearThenInsert(schedName, jobTriggerInfos);
                    handledScheduler.add(schedName);
                } catch (Exception e) {
                    errorHandlerChain.handle(instance, e);
                }
            }
        });
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
        jobTriggerInfoRepository.deleteBySchedName(schedName);

        // insert
        for (QuartzJobTriggerInfo dto : jobTriggerInfos) {
            QuartzSchedulerJobTriggerInfo info = QuartzSchedulerJobTriggerInfo.valueOf(dto);
            jobTriggerInfoRepository.save(info);
        }
    }
}
