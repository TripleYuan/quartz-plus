package redcoder.quartzplus.schedcenter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.quartz.CronExpression;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import redcoder.quartzplus.common.utils.HttpTemplate;
import redcoder.quartzplus.common.utils.JsonUtils;
import redcoder.quartzplus.common.utils.MapUtils;
import redcoder.quartzplus.core.core.dto.QuartzJobTriggerInfo;
import redcoder.quartzplus.schedcenter.constant.QuartzApiConstants;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.PageResponse;
import redcoder.quartzplus.schedcenter.dto.job.*;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusInstance;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusJobExecutionRecord;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusJobTriggerInfo;
import redcoder.quartzplus.schedcenter.entity.key.QuartzPlusJobTriggerInfoKey;
import redcoder.quartzplus.schedcenter.exception.JobManageException;
import redcoder.quartzplus.schedcenter.repository.InstanceRepository;
import redcoder.quartzplus.schedcenter.repository.JobExecutionRecordRepository;
import redcoder.quartzplus.schedcenter.repository.JobTriggerInfoRepository;

import javax.annotation.Resource;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@Service
public class QuartzJobManageServiceImpl implements QuartzJobManageService {

    private static final String OK = "OK";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Resource
    private JobTriggerInfoRepository jobTriggerInfoRepository;
    @Resource
    private InstanceRepository instanceRepository;
    @Resource
    private JobExecutionRecordRepository recordRepository;

    @Override
    public List<String> getSchedNames() {
        return jobTriggerInfoRepository.findAllSchedName();
    }

    @Override
    public PageResponse<JobTriggerDTO> getJobTriggerInfos(QueryJobTriggerInfo dto) {
        String schedName = dto.getSchedName();
        String search = dto.getJobName();
        int pageNo = dto.getPageNo() - 1;
        int pageSize = dto.getPageSize();

        QuartzPlusJobTriggerInfo probe = new QuartzPlusJobTriggerInfo();
        if (hasText(schedName)) {
            probe.setSchedName(schedName);
        }
        if (hasText(search)) {
            probe.setJobName(search);
        }
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("jobName", matcher -> matcher.contains());
        Example<QuartzPlusJobTriggerInfo> example = Example.of(probe, exampleMatcher);
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Order.asc("schedName"), Order.asc("jobName")));
        Page<QuartzPlusJobTriggerInfo> page = jobTriggerInfoRepository.findAll(example, pageRequest);

        List<JobTriggerDTO> data = page.getContent().stream()
                .map(t -> {
                    JobTriggerDTO jobTriggerDTO = new JobTriggerDTO();
                    // 属性赋值
                    BeanUtils.copyProperties(t, jobTriggerDTO);
                    return jobTriggerDTO;
                })
                .collect(Collectors.toList());
        return new PageResponse<>(page.getTotalElements(), pageNo, pageSize, data);
    }

    @Override
    public JobTriggerDTO refreshJobTrigger(RefreshJobTriggerDTO dto) {
        // 刷新数据
        refreshJobTriggerInfoInternal(dto);

        // 获取刷新后的数据
        QuartzPlusJobTriggerInfoKey key =
                new QuartzPlusJobTriggerInfoKey(dto.getSchedName(), dto.getTriggerName(), dto.getTriggerGroup());
        QuartzPlusJobTriggerInfo jobTriggerInfo = jobTriggerInfoRepository.findById(key).orElseThrow(() -> new JobManageException("刷新Job失败"));

        JobTriggerDTO jobTriggerDTO = new JobTriggerDTO();
        BeanUtils.copyProperties(jobTriggerInfo, jobTriggerDTO);
        return jobTriggerDTO;
    }

    private void refreshJobTriggerInfoInternal(RefreshJobTriggerDTO dto) {
        QuartzPlusInstance instance = getQuartzSchedulerInstance(dto.getSchedName());

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
        QuartzPlusJobTriggerInfo info = QuartzPlusJobTriggerInfo.valueOf(result.getData());
        jobTriggerInfoRepository.save(info);
    }

    @Override
    public void removeLocal(RemoveLocalJobTriggerDTO dto) {
        QuartzPlusJobTriggerInfoKey key = new QuartzPlusJobTriggerInfoKey();
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

        QuartzPlusInstance instance = getQuartzSchedulerInstance(scheduleJobDto.getSchedName());
        String url = "http://" + instance.getInstanceHost() + ":" + instance.getInstancePort() + QuartzApiConstants.SCHEDULE_JOB;
        Map<String, String> body = MapUtils.buildMap("jobName", scheduleJobDto.getJobName(),
                "jobGroup", scheduleJobDto.getJobGroup(), "cron", cron);
        ApiResult<Boolean> result = HttpTemplate.doPost(url, JsonUtils.toJsonString(body),
                new TypeReference<ApiResult<Boolean>>() {
                });
        if (result.getStatus() == 0 && Boolean.TRUE.equals(result.getData())) {
            return;
        }
        throw new JobManageException("修改job失败：" + result.getMessage());
    }

    @Override
    public void saveJobExecutionRecord(JobExecutionRecordDto dto) {
        QuartzPlusJobExecutionRecord record = new QuartzPlusJobExecutionRecord();
        record.setSchedName(dto.getSchedName());
        record.setJobName(dto.getJobName());
        record.setJobGroup(dto.getJobGroup());
        record.setStatus(dto.getStatus());
        record.setStartTime(LocalDateTime.parse(dto.getStartTime(), DATE_TIME_FORMATTER));
        record.setEndTime(LocalDateTime.parse(dto.getEndTime(), DATE_TIME_FORMATTER));
        record.setCostTime(dto.getCostTime());
        record.setException(dto.getException());
        recordRepository.save(record);
    }

    @Override
    public PageResponse<JobExecutionRecordDto> getJobExecutionRecord(QueryJobTriggerInfo dto) {
        String schedName = dto.getSchedName();
        String search = dto.getJobName();
        int pageNo = dto.getPageNo() - 1;
        int pageSize = dto.getPageSize();

        QuartzPlusJobExecutionRecord probe = new QuartzPlusJobExecutionRecord();
        if (hasText(schedName)) {
            probe.setSchedName(schedName);
        }
        if (hasText(search)) {
            probe.setJobName(search);
        }
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("jobName", matcher -> matcher.contains());
        Example<QuartzPlusJobExecutionRecord> example = Example.of(probe, exampleMatcher);
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Order.desc("id")));
        Page<QuartzPlusJobExecutionRecord> page = recordRepository.findAll(example, pageRequest);

        List<JobExecutionRecordDto> data = page.getContent().stream()
                .map(t -> {
                    JobExecutionRecordDto recordDto = new JobExecutionRecordDto();
                    BeanUtils.copyProperties(t, recordDto);
                    recordDto.setStartTime(t.getStartTime().format(DATE_TIME_FORMATTER));
                    recordDto.setEndTime(t.getEndTime().format(DATE_TIME_FORMATTER));
                    return recordDto;
                })
                .collect(Collectors.toList());
        return new PageResponse<>(page.getTotalElements(), pageNo, pageSize, data);
    }

    private String executeCommand(JobManageDTO jobManageDTO, String api) {
        QuartzPlusInstance instance = getQuartzSchedulerInstance(jobManageDTO.getSchedName());
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

    private QuartzPlusInstance getQuartzSchedulerInstance(String schedName) {
        return instanceRepository.findTopBySchedName(schedName);
    }
}

