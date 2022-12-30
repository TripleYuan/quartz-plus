package redcoder.quartzextendschedulercenter.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import org.quartz.CronExpression;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redcoder.quartzextendcommon.utils.HttpTemplate;
import redcoder.quartzextendcommon.utils.JsonUtils;
import redcoder.quartzextendcommon.utils.MapUtils;
import redcoder.quartzextendcore.core.dto.QuartzJobTriggerInfo;
import redcoder.quartzextendschedulercenter.constant.QuartzApiConstants;
import redcoder.quartzextendschedulercenter.dto.ApiResult;
import redcoder.quartzextendschedulercenter.dto.PageResponse;
import redcoder.quartzextendschedulercenter.dto.job.*;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerInstance;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerJobTriggerInfo;
import redcoder.quartzextendschedulercenter.entity.key.QuartzSchedulerJobTriggerInfoKey;
import redcoder.quartzextendschedulercenter.exception.JobManageException;
import redcoder.quartzextendschedulercenter.repository.InstanceRepository;
import redcoder.quartzextendschedulercenter.repository.JobTriggerInfoRepository;
import redcoder.quartzextendschedulercenter.service.QuartzJobManageService;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author redcoder54
 * @since 2022-01-09
 */
@Service
public class QuartzJobManageServiceImpl implements QuartzJobManageService {

    private static final String OK = "OK";

    @Resource
    private JobTriggerInfoRepository jobTriggerInfoRepository;
    @Resource
    private InstanceRepository instanceRepository;

    @Override
    public List<String> getSchedNames() {
        return jobTriggerInfoRepository.findAllSchedName();
    }

    @Override
    public PageResponse<JobTriggerDTO> getJobTriggerInfos(QueryJobTriggerInfo queryJobTriggerInfo) {
        String schedName = queryJobTriggerInfo.getSchedName();
        String jobName = queryJobTriggerInfo.getJobName();
        int pageNo = queryJobTriggerInfo.getPageNo() - 1;
        int pageSize = queryJobTriggerInfo.getPageSize();

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Order.asc("schedName"), Order.asc("jobName")));
        Page<QuartzSchedulerJobTriggerInfo> page;
        if (StringUtils.hasText(schedName) && StringUtils.hasText(jobName)) {
            page = jobTriggerInfoRepository.findBySchedNameAndJobNameLike(schedName, "%" + jobName + "%", pageRequest);
        } else if (StringUtils.hasText(schedName)) {
            page = jobTriggerInfoRepository.findBySchedName(schedName, pageRequest);
        } else if (StringUtils.hasText(jobName)) {
            page = jobTriggerInfoRepository.findByJobNameLike("%" + jobName + "%", pageRequest);
        } else {
            page = jobTriggerInfoRepository.findAll(pageRequest);
        }

        List<JobTriggerDTO> data = page.stream()
                .map(t -> {
                    JobTriggerDTO dto = new JobTriggerDTO();
                    // 属性赋值
                    BeanUtils.copyProperties(t, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        return new PageResponse<>(page.getTotalElements(), pageNo, pageSize, data);
    }

    @Override
    public JobTriggerDTO refreshJobTrigger(RefreshJobTriggerDTO dto) {
        // 刷新数据
        refreshJobTriggerInfoInternal(dto);

        // 获取刷新后的数据
        QuartzSchedulerJobTriggerInfoKey key =
                new QuartzSchedulerJobTriggerInfoKey(dto.getSchedName(), dto.getTriggerName(), dto.getTriggerGroup());
        QuartzSchedulerJobTriggerInfo jobTriggerInfo = jobTriggerInfoRepository.findById(key).orElseThrow(() -> new JobManageException("刷新Job失败"));

        JobTriggerDTO jobTriggerDTO = new JobTriggerDTO();
        BeanUtils.copyProperties(jobTriggerInfo, jobTriggerDTO);
        return jobTriggerDTO;
    }

    private void refreshJobTriggerInfoInternal(RefreshJobTriggerDTO dto) {
        QuartzSchedulerInstance instance = getQuartzSchedulerInstance(dto.getSchedName());

        String url = "http://" + instance.getInstanceHost() + ":" + instance.getInstancePort() + QuartzApiConstants.JOB_TRIGGER_INFO_QUERY;
        Map<String, String> queryParams = MapUtils.buildMap("triggerName", dto.getTriggerName(),
                "triggerGroup", dto.getTriggerGroup());
        ApiResult<QuartzJobTriggerInfo> result = HttpTemplate.doGet(url, queryParams,
                new TypeReference<ApiResult<QuartzJobTriggerInfo>>() {
                });
        if (result.getStatus() != 0) {
            throw new JobManageException("刷新job和trigger信息失败：" + result.getMessage());
        }

        // 更新数据
        QuartzSchedulerJobTriggerInfo info = QuartzSchedulerJobTriggerInfo.valueOf(result.getData());
        jobTriggerInfoRepository.save(info);
    }

    @Override
    public void removeLocal(RemoveLocalJobTriggerDTO dto) {
        QuartzSchedulerJobTriggerInfoKey key = new QuartzSchedulerJobTriggerInfoKey();
        key.setSchedName(dto.getSchedName());
        key.setTriggerName(dto.getTriggerName());
        key.setTriggerGroup(dto.getTriggerGroup());
        jobTriggerInfoRepository.deleteById(key);
    }

    @Override
    public void triggerJob(JobManageDTO jobManageDTO) {
        String result = executeCommand(jobManageDTO, QuartzApiConstants.TRIGGER_JOB);
        if (OK.equals(result)) {
            return;
        }
        throw new JobManageException("触发job失败：" + result);
    }

    @Override
    public void pauseJob(JobManageDTO jobManageDTO) {
        String result = executeCommand(jobManageDTO, QuartzApiConstants.PAUSE_JOB);
        if (OK.equals(result)) {
            return;
        }
        throw new JobManageException("暂停job失败：" + result);
    }

    @Override
    public void resumeJob(JobManageDTO jobManageDTO) {
        String result = executeCommand(jobManageDTO, QuartzApiConstants.RESUME_JOB);
        if (OK.equals(result)) {
            return;
        }
        throw new JobManageException("恢复job失败：" + result);
    }

    @Override
    public void deleteJob(JobManageDTO dto) {
        String result = executeCommand(dto, QuartzApiConstants.DELETE_JOB);
        if (OK.equals(result)) {
            jobTriggerInfoRepository.deleteBySchedNameAndJobNameAndJobGroup(dto.getSchedName(), dto.getJobName(), dto.getJobGroup());
            return;
        }
        throw new JobManageException("删除job失败：" + result);
    }

    @Override
    public void scheduleJob(ScheduleJobDto scheduleJobDto) {
        // 校验cron表达式是否有效
        String cron = scheduleJobDto.getCron();
        try{
            new CronExpression(cron);
        }catch (ParseException e){
            throw new JobManageException(String.format("无效的cron表达式 \"%s\"", cron));
        }

        QuartzSchedulerInstance instance = getQuartzSchedulerInstance(scheduleJobDto.getSchedName());
        String url = "http://" + instance.getInstanceHost() + ":" + instance.getInstancePort() + QuartzApiConstants.SCHEDULE_JOB;
        Map<String, String> body = MapUtils.buildMap("jobName", scheduleJobDto.getJobName(),
                "jobGroup", scheduleJobDto.getJobGroup(), "cron", cron);
        ApiResult<Boolean> result = HttpTemplate.doPost(url, JsonUtils.mapToJsonString(body),
                new TypeReference<ApiResult<Boolean>>() {
                });
        if (result.getStatus() == 0 && Boolean.TRUE.equals(result.getData())) {
            return;
        }
        throw new JobManageException("修改job失败：" + result.getMessage());
    }

    private String executeCommand(JobManageDTO jobManageDTO, String api) {
        QuartzSchedulerInstance instance = getQuartzSchedulerInstance(jobManageDTO.getSchedName());
        String url = "http://" + instance.getInstanceHost() + ":" + instance.getInstancePort() + api;
        Map<String, String> formParams = MapUtils.buildMap("jobName", jobManageDTO.getJobName(),
                "jobGroup", jobManageDTO.getJobGroup());
        ApiResult<Boolean> result = HttpTemplate.doPost(url, formParams,
                new TypeReference<ApiResult<Boolean>>() {
                });
        if (result.getStatus() == 0 && Boolean.TRUE.equals(result.getData())) {
            return OK;
        }
        return result.getMessage();
    }

    private QuartzSchedulerInstance getQuartzSchedulerInstance(String schedName) {
        return instanceRepository.findTopBySchedName(schedName);
    }
}

