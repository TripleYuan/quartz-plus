package redcoder.quartzextendschedulercenter.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import redcoder.quartzextendcommon.utils.HttpTemplate;
import redcoder.quartzextendcommon.utils.MapUtils;
import redcoder.quartzextendcore.core.dto.QuartzJobTriggerInfo;
import redcoder.quartzextendschedulercenter.dto.job.*;
import redcoder.quartzextendschedulercenter.exception.JobManageException;
import redcoder.quartzextendschedulercenter.mapper.QuartzSchedulerInstanceMapper;
import redcoder.quartzextendschedulercenter.mapper.QuartzSchedulerJobTriggerInfoMapper;
import redcoder.quartzextendschedulercenter.dto.ApiResult;
import redcoder.quartzextendschedulercenter.dto.PageResponse;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerInstance;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerJobTriggerInfo;
import redcoder.quartzextendschedulercenter.service.QuartzJobManageService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redcoder.quartzextendschedulercenter.constant.QuartzApiConstants;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author redcoder54
 * @since 2022-01-09
 */
@Service
public class QuartzJobManageServiceImpl implements QuartzJobManageService {

    private static final String OK = "OK";

    @Resource
    private QuartzSchedulerInstanceMapper instanceMapper;
    @Resource
    private QuartzSchedulerJobTriggerInfoMapper infoMapper;

    @Override
    public List<String> getSchedNames() {
        Example example = Example.builder(QuartzSchedulerJobTriggerInfo.class)
                .select("schedName")
                .distinct()
                .orderByDesc("schedName")
                .build();
        List<QuartzSchedulerJobTriggerInfo> list = infoMapper.selectByExample(example);
        return list.stream().map(QuartzSchedulerJobTriggerInfo::getSchedName).collect(Collectors.toList());
    }

    @Override
    public PageResponse<JobTriggerDTO> getJobTriggerInfos(QueryJobTriggerInfo queryJobTriggerInfo) {
        String schedName = queryJobTriggerInfo.getSchedName();
        String jobName = queryJobTriggerInfo.getJobName();
        int pageNo = queryJobTriggerInfo.getPageNo();
        int pageSize = queryJobTriggerInfo.getPageSize();

        Example.Builder builder = Example.builder(QuartzSchedulerJobTriggerInfo.class)
                .orderByDesc("schedName")
                .orderByAsc("jobName");
        if (StringUtils.hasText(schedName)) {
            builder.andWhere(Sqls.custom().andEqualTo("schedName", schedName));
        }
        if (StringUtils.hasText(jobName)) {
            builder.andWhere(Sqls.custom().andLike("jobName", "%" + jobName + "%"));
        }
        Example example = builder.build();

        PageHelper.startPage(pageNo, pageSize);
        List<QuartzSchedulerJobTriggerInfo> list = infoMapper.selectByExample(example);
        Page<QuartzSchedulerJobTriggerInfo> page = (Page<QuartzSchedulerJobTriggerInfo>) list;
        List<JobTriggerDTO> data = page.stream()
                .map(t -> {
                    JobTriggerDTO dto = new JobTriggerDTO();
                    // 属性赋值
                    BeanUtils.copyProperties(t, dto);
                    return dto;
                })
                .collect(Collectors.toList());

        return new PageResponse<>(page.getTotal(), pageNo, pageSize, data);
    }

    @Override
    public JobTriggerDTO refreshJobTrigger(RefreshJobTriggerDTO dto) {
        // 刷新数据
        refreshJobTriggerInfoInternal(dto);
        // 获取刷新后的数据
        Example example = Example.builder(QuartzSchedulerJobTriggerInfo.class)
                .andWhere(Sqls.custom()
                        .andEqualTo("schedName", dto.getSchedName())
                        .andEqualTo("triggerName", dto.getTriggerName())
                        .andEqualTo("triggerGroup", dto.getTriggerGroup()))
                .build();
        QuartzSchedulerJobTriggerInfo jobTriggerInfo = infoMapper.selectOneByExample(example);

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
        info.setUpdateTime(new Date());
        infoMapper.updateByPrimaryKeySelective(info);
    }

    @Override
    public boolean removeLocal(RemoveLocalJobTriggerDTO dto) {
        QuartzSchedulerJobTriggerInfo info = new QuartzSchedulerJobTriggerInfo();
        info.setSchedName(dto.getSchedName());
        info.setTriggerName(dto.getTriggerName());
        info.setTriggerGroup(dto.getTriggerGroup());
        int i = infoMapper.deleteByPrimaryKey(info);
        return i > 0;
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
    public void deleteJob(JobManageDTO jobManageDTO) {
        String result = executeCommand(jobManageDTO, QuartzApiConstants.DELETE_JOB);
        if (OK.equals(result)) {
            QuartzSchedulerJobTriggerInfo info = new QuartzSchedulerJobTriggerInfo();
            info.setSchedName(jobManageDTO.getSchedName());
            info.setJobName(jobManageDTO.getJobName());
            info.setJobGroup(jobManageDTO.getJobGroup());
            infoMapper.delete(info);
            return;
        }
        throw new JobManageException("删除job失败：" + result);
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
        Example example = Example.builder(QuartzSchedulerInstance.class)
                .select("instanceHost", "instancePort")
                .where(Sqls.custom().andEqualTo("schedName", schedName))
                .build();
        List<QuartzSchedulerInstance> instances = instanceMapper.selectByExample(example);
        return instances.get(0);
    }
}
