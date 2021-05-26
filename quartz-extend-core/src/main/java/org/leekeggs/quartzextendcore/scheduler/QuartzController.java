package org.leekeggs.quartzextendcore.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.leekeggs.quartzextendcore.core.dto.QuartzApiResult;
import org.leekeggs.quartzextendcore.core.dto.QuartzJobTriggerInfo;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * @author leekeggs
 * @since 1.0.0
 */
@RestController
@RequestMapping(value = "/quartz", produces = APPLICATION_JSON_VALUE)
@Slf4j
public class QuartzController {

    private QuartzService quartzService;

    public QuartzController(QuartzService quartzService) {
        this.quartzService = quartzService;
    }

    @GetMapping("/job-trigger-info/list")
    public QuartzApiResult<List<QuartzJobTriggerInfo>> getQuartzJobTriggerInfoList() {
        try {
            List<QuartzJobTriggerInfo> quartzJobTriggerInfoList = quartzService.getQuartzJobTriggerInfoList();
            return new QuartzApiResult<>(0, "", quartzJobTriggerInfoList);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return new QuartzApiResult<>(500, e.getMessage());
        }
    }

    @GetMapping("/job-trigger-info/query")
    public QuartzApiResult<QuartzJobTriggerInfo> getQuartzJobTriggerInfo(@RequestParam String triggerName, @RequestParam String triggerGroup) {
        try {
            QuartzJobTriggerInfo quartzJobTriggerInfo = quartzService.getQuartzJobTriggerInfo(triggerName, triggerGroup);
            return new QuartzApiResult<>(0, "", quartzJobTriggerInfo);
        } catch (SchedulerException e) {
            log.warn(e.getMessage(), e);
            return new QuartzApiResult<>(500, e.getMessage());
        }
    }

    @PostMapping("/trigger-job")
    public QuartzApiResult<Boolean> triggerJob(@RequestParam String jobName, @RequestParam String jobGroup) {
        try {
            quartzService.triggerJob(jobName, jobGroup);
            return new QuartzApiResult<>(0, "", true);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
            return new QuartzApiResult<>(500, e.getMessage(), false);
        }
    }
}
