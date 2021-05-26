package org.leekeggs.quartzextendschedulercenter.job;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.leekeggs.quartzextendcommon.utils.HttpTemplate;
import org.leekeggs.quartzextendcommon.utils.JsonUtils;
import org.leekeggs.quartzextendcore.annotation.QuartzJob;
import org.leekeggs.quartzextendcore.annotation.QuartzTrigger;
import org.leekeggs.quartzextendschedulercenter.mapper.QuartzSchedulerInstanceMapper;
import org.leekeggs.quartzextendschedulercenter.mapper.QuartzSchedulerJobTriggerInfoMapper;
import org.leekeggs.quartzextendschedulercenter.model.dto.ApiResult;
import org.leekeggs.quartzextendschedulercenter.model.dto.QuartzSchedulerJobTriggerInfoDTO;
import org.leekeggs.quartzextendschedulercenter.model.entity.QuartzSchedulerInstance;
import org.leekeggs.quartzextendschedulercenter.model.entity.QuartzSchedulerJobTriggerInfo;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.leekeggs.quartzextendschedulercenter.constant.QuartzApiConstants.QUARTZ_JOB_TRIGGER_INFO_LIST;

/**
 * 采集quartz job和trigger信息，保存到数据库表中
 *
 * @author leekeggs
 * @since 2021-04-25
 */
@Slf4j
@QuartzJob(jobDescription = "采集quartz job和trigger信息，保存到数据库表中")
@QuartzTrigger(triggerDescription = "JobTriggerInfoCollectingJob's trigger", cronExpress = "0 0/1 * * * ?")
public class JobTriggerInfoCollectingJob extends QuartzJobBean {

    @Resource
    private QuartzSchedulerInstanceMapper instanceMapper;
    @Resource
    private QuartzSchedulerJobTriggerInfoMapper infoMapper;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        try {
            List<QuartzSchedulerInstance> instances = instanceMapper.selectAll();
            for (QuartzSchedulerInstance instance : instances) {
                try {
                    List<QuartzSchedulerJobTriggerInfoDTO> infoDTOList = getJobTriggerInfo(instance);
                    insertOrUpdate(infoDTOList);
                } catch (Exception e) {
                    log.error("收集数据失败: " + JsonUtils.beanToJsonString(instance), e);
                }
            }
        } catch (Exception e) {
            log.error("JobTriggerInfoCollectingJob error", e);
        }
    }

    private List<QuartzSchedulerJobTriggerInfoDTO> getJobTriggerInfo(QuartzSchedulerInstance instance) {
        String url = "http://" + instance.getInstanceHost() + ":" + instance.getInstancePort() + QUARTZ_JOB_TRIGGER_INFO_LIST;
        ApiResult<List<QuartzSchedulerJobTriggerInfoDTO>> result = HttpTemplate.doGet(url, new TypeReference<ApiResult<List<QuartzSchedulerJobTriggerInfoDTO>>>() {
        });
        if (result.getStatus() != 0) {
            log.error("获取QuartzSchedulerJobTriggerInfo失败，原因：{}", result.getMessage());
            return Collections.emptyList();
        }
        return result.getData();
    }

    /**
     * 新增或更新数据
     *
     * @param dtoList job和trigger数据
     */
    private void insertOrUpdate(List<QuartzSchedulerJobTriggerInfoDTO> dtoList) {
        for (QuartzSchedulerJobTriggerInfoDTO dto : dtoList) {
            QuartzSchedulerJobTriggerInfo info = QuartzSchedulerJobTriggerInfo.valueOf(dto);
            if (infoMapper.selectByPrimaryKey(info) != null) {
                // 更新数据
                info.setUpdateTime(new Date());
                infoMapper.updateByPrimaryKeySelective(info);
            } else {
                info.setCreateTime(new Date());
                info.setUpdateTime(new Date());
                infoMapper.insertSelective(info);
            }
        }
    }
}
