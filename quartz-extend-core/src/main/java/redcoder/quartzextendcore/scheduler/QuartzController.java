package redcoder.quartzextendcore.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;
import redcoder.quartzextendcore.core.dto.QuartzApiResult;
import redcoder.quartzextendcore.core.dto.QuartzJobTriggerInfo;
import redcoder.quartzextendcore.core.dto.ScheduleJob;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * @author redcoder54
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
            log.error(e.getMessage(), e);
            return new QuartzApiResult<>(500, e.getMessage());
        }
    }

    @GetMapping("/job-trigger-info/query")
    public QuartzApiResult<QuartzJobTriggerInfo> getQuartzJobTriggerInfo(@RequestParam String triggerName, @RequestParam String triggerGroup) {
        try {
            QuartzJobTriggerInfo quartzJobTriggerInfo = quartzService.getQuartzJobTriggerInfo(triggerName, triggerGroup);
            return new QuartzApiResult<>(0, "", quartzJobTriggerInfo);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return new QuartzApiResult<>(500, e.getMessage());
        }
    }

    @PostMapping("/trigger-job")
    public QuartzApiResult<Boolean> triggerJob(@RequestParam String jobName, @RequestParam String jobGroup) {
        try {
            quartzService.triggerJob(jobName, jobGroup);
            return new QuartzApiResult<>(0, "", true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new QuartzApiResult<>(500, e.getMessage(), false);
        }
    }

    @PostMapping("/pause-job")
    public QuartzApiResult<Boolean> pauseJob(@RequestParam String jobName, @RequestParam String jobGroup) {
        try {
            quartzService.pauseJob(jobName, jobGroup);
            return new QuartzApiResult<>(0, "", true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new QuartzApiResult<>(500, e.getMessage(), false);
        }
    }

    @PostMapping("/resume-job")
    public QuartzApiResult<Boolean> resumeJob(@RequestParam String jobName, @RequestParam String jobGroup) {
        try {
            quartzService.resumeJob(jobName, jobGroup);
            return new QuartzApiResult<>(0, "", true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new QuartzApiResult<>(500, e.getMessage(), false);
        }
    }

    @PostMapping("/delete-job")
    public QuartzApiResult<Boolean> deleteJob(@RequestParam String jobName, @RequestParam String jobGroup) {
        try {
            quartzService.deleteJob(jobName, jobGroup);
            return new QuartzApiResult<>(0, "", true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new QuartzApiResult<>(500, e.getMessage(), false);
        }
    }

    @PostMapping("/schedule-job")
    public QuartzApiResult<Boolean> scheduleJob(@RequestBody ScheduleJob scheduleJob) {
        try {
            quartzService.scheduleJob(scheduleJob);
            return new QuartzApiResult<>(0, "", true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new QuartzApiResult<>(500, e.getMessage(), false);
        }
    }
}
