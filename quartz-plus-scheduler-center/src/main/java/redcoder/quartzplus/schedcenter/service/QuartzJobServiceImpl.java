package redcoder.quartzplus.schedcenter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.quartz.CronExpression;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import redcoder.quartzplus.common.utils.HttpTemplate;
import redcoder.quartzplus.common.utils.JsonUtils;
import redcoder.quartzplus.common.utils.MapUtils;
import redcoder.quartzplus.core.core.dto.QuartzJobInfo;
import redcoder.quartzplus.schedcenter.constant.QuartzApiConstants;
import redcoder.quartzplus.schedcenter.dto.ApiResult;
import redcoder.quartzplus.schedcenter.dto.PageResponse;
import redcoder.quartzplus.schedcenter.dto.job.*;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusInstance;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusJobExecutionRecord;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusJobInfo;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusJobInfoSpec;
import redcoder.quartzplus.schedcenter.entity.key.QuartzPlusJobInfoKey;
import redcoder.quartzplus.schedcenter.exception.JobOperationException;
import redcoder.quartzplus.schedcenter.repository.InstanceRepository;
import redcoder.quartzplus.schedcenter.repository.JobExecutionRecordRepository;
import redcoder.quartzplus.schedcenter.repository.JobInfoRepository;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;
import static redcoder.quartzplus.schedcenter.constant.QuartzApiConstants.*;

@Service
public class QuartzJobServiceImpl implements QuartzJobService {

    private static final String OK = "OK";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private JobInfoRepository jobInfoRepository;
    private InstanceRepository instanceRepository;
    private JobExecutionRecordRepository recordRepository;

    public QuartzJobServiceImpl(JobInfoRepository jobInfoRepository,
                                InstanceRepository instanceRepository,
                                JobExecutionRecordRepository recordRepository) {
        this.jobInfoRepository = jobInfoRepository;
        this.instanceRepository = instanceRepository;
        this.recordRepository = recordRepository;
    }

    @Override
    public List<String> getSchedNames() {
        return jobInfoRepository.findAllSchedName();
    }

    @Override
    public PageResponse<JobInfo> getJobInfo(JobInfoQuery dto) {
        String schedName = dto.getSchedName();
        String search = dto.getJobName();
        int pageNo = dto.getPageNo() - 1;
        int pageSize = dto.getPageSize();

        Specification<QuartzPlusJobInfo> specification = QuartzPlusJobInfoSpec.jobQuery(schedName, search);
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Order.asc("schedName"), Order.asc("jobName")));
        Page<QuartzPlusJobInfo> page = jobInfoRepository.findAll(specification, pageRequest);
        List<JobInfo> data = page.getContent().stream()
                .map(t -> {
                    JobInfo jobInfo = new JobInfo();
                    BeanUtils.copyProperties(t, jobInfo);
                    return jobInfo;
                })
                .collect(Collectors.toList());
        return new PageResponse<>(page.getTotalElements(), pageNo, pageSize, data);
    }

    @Override
    public JobInfo refreshJob(JobUniqueId jobUniqueId) {
        QuartzPlusJobInfoKey key = new QuartzPlusJobInfoKey(jobUniqueId.getSchedName(), jobUniqueId.getJobName(), jobUniqueId.getJobGroup());
        QuartzPlusJobInfo info = jobInfoRepository.findById(key).orElseThrow(() -> new JobOperationException("Job不存在"));

        // 刷新数据
        refreshJobTriggerInfoInternal(info.getSchedName(), info.getTriggerName(), info.getTriggerGroup());

        // 获取刷新后的数据
        info = jobInfoRepository.findById(key).orElseThrow(() -> new JobOperationException("刷新Job失败"));

        JobInfo jobInfo = new JobInfo();
        BeanUtils.copyProperties(info, jobInfo);
        return jobInfo;
    }

    private void refreshJobTriggerInfoInternal(String schedName, String triggerName, String triggerGroup) {
        QuartzPlusInstance instance = getQuartzSchedulerInstance(schedName);

        String url = "http://" + instance.getInstanceHost() + ":" + instance.getInstancePort() + QuartzApiConstants.QUERY_JOB;
        Map<String, String> queryParams = MapUtils.buildMap("triggerName", triggerName, "triggerGroup", triggerGroup);
        ApiResult<QuartzJobInfo> result = HttpTemplate.doGet(url, queryParams,
                new TypeReference<ApiResult<QuartzJobInfo>>() {
                });
        if (result.getStatus() != 0) {
            throw new JobOperationException("刷新job和trigger信息失败：" + result.getMessage());
        }

        // 更新数据
        QuartzPlusJobInfo info = QuartzPlusJobInfo.valueOf(result.getData());
        jobInfoRepository.save(info);
    }

    @Override
    public void removeLocal(JobUniqueId jobUniqueId) {
        QuartzPlusJobInfoKey key = new QuartzPlusJobInfoKey();
        key.setSchedName(jobUniqueId.getSchedName());
        key.setJobName(jobUniqueId.getJobName());
        key.setJobGroup(jobUniqueId.getJobGroup());
        jobInfoRepository.deleteById(key);
    }

    @Override
    public void executeJob(JobUniqueId jobUniqueId) {
        requestScheduler(jobUniqueId, EXECUTE_JOB);
    }

    @Override
    public void pauseJob(JobUniqueId jobUniqueId) {
        requestScheduler(jobUniqueId, PAUSE_JOB);
    }

    @Override
    public void resumeJob(JobUniqueId jobUniqueId) {
        requestScheduler(jobUniqueId, RESUME_JOB);
    }

    @Override
    public void deleteJob(JobUniqueId jobUniqueId) {
        QuartzPlusInstance instance = getQuartzSchedulerInstance(jobUniqueId.getSchedName());
        String url = "http://" + instance.getInstanceHost() + ":" + instance.getInstancePort() + DELETE_JOB
                + "/" + jobUniqueId.getJobName() + "/" + jobUniqueId.getJobGroup();
        ApiResult<String> result = HttpTemplate.doDelete(url, new TypeReference<ApiResult<String>>() {
        });
        if (result.getStatus() == 0) {
            removeLocal(jobUniqueId);
            return;
        }
        throw new JobOperationException(String.format("请求失败，uri: %s, status: %s, message: %s", DELETE_JOB, result.getStatus(), result.getMessage()));
    }

    private void requestScheduler(JobUniqueId jobUniqueId, String uri) {
        QuartzPlusInstance instance = getQuartzSchedulerInstance(jobUniqueId.getSchedName());
        String url = "http://" + instance.getInstanceHost() + ":" + instance.getInstancePort() + uri
                + "/" + jobUniqueId.getJobName() + "/" + jobUniqueId.getJobGroup();
        ApiResult<String> result = HttpTemplate.doPost(url, Collections.emptyMap(),
                new TypeReference<ApiResult<String>>() {
                });
        if (result.getStatus() == 0) {
            return;
        }
        throw new JobOperationException(String.format("请求失败，uri: %s, status: %s, message: %s", uri, result.getStatus(), result.getMessage()));
    }

    @Override
    public void updateJob(JobInfoUpdate jobInfoUpdate) {
        // 校验cron表达式是否有效
        String cron = jobInfoUpdate.getCron();
        try {
            new CronExpression(cron);
        } catch (ParseException e) {
            throw new JobOperationException(String.format("无效的cron表达式 \"%s\"", cron));
        }

        QuartzPlusInstance instance = getQuartzSchedulerInstance(jobInfoUpdate.getSchedName());
        String url = "http://" + instance.getInstanceHost() + ":" + instance.getInstancePort() + QuartzApiConstants.UPDATE_JOB;
        Map<String, String> body = MapUtils.buildMap("jobName", jobInfoUpdate.getJobName(),
                "jobGroup", jobInfoUpdate.getJobGroup(), "cron", cron);
        ApiResult<Boolean> result = HttpTemplate.doPost(url, JsonUtils.toJsonString(body),
                new TypeReference<ApiResult<Boolean>>() {
                });
        if (result.getStatus() == 0 && Boolean.TRUE.equals(result.getData())) {
            return;
        }
        throw new JobOperationException("修改job失败：" + result.getMessage());
    }

    @Override
    public void saveJobExecutionRecord(JobExecutionRecord dto) {
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
    public PageResponse<JobExecutionRecord> getJobExecutionRecord(JobInfoQuery dto) {
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
                .withMatcher("jobName", ExampleMatcher.GenericPropertyMatcher::contains);
        Example<QuartzPlusJobExecutionRecord> example = Example.of(probe, exampleMatcher);
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Order.desc("id")));
        Page<QuartzPlusJobExecutionRecord> page = recordRepository.findAll(example, pageRequest);

        List<JobExecutionRecord> data = page.getContent().stream()
                .map(t -> {
                    JobExecutionRecord recordDto = new JobExecutionRecord();
                    BeanUtils.copyProperties(t, recordDto);
                    recordDto.setStartTime(t.getStartTime().format(DATE_TIME_FORMATTER));
                    recordDto.setEndTime(t.getEndTime().format(DATE_TIME_FORMATTER));
                    return recordDto;
                })
                .collect(Collectors.toList());
        return new PageResponse<>(page.getTotalElements(), pageNo, pageSize, data);
    }

    private QuartzPlusInstance getQuartzSchedulerInstance(String schedName) {
        return instanceRepository.findTopBySchedName(schedName);
    }
}

