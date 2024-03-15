package redcoder.quartzplus.core.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;
import redcoder.quartzplus.core.core.dto.QuartzApiResult;
import redcoder.quartzplus.core.core.dto.QuartzJobInfo;
import redcoder.quartzplus.core.core.dto.JobUniqueId;

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

    @GetMapping("/jobs")
    public QuartzApiResult<List<QuartzJobInfo>> getQuartzJobs() {
        try {
            List<QuartzJobInfo> quartzJobInfoList = quartzService.getQuartzJobs();
            return new QuartzApiResult<>(0, "", quartzJobInfoList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new QuartzApiResult<>(500, e.getMessage());
        }
    }

    @GetMapping("/job")
    public QuartzApiResult<QuartzJobInfo> queryJob(@RequestParam String triggerName,
                                                   @RequestParam String triggerGroup) {
        try {
            QuartzJobInfo quartzJobInfo = quartzService.queryJob(triggerName, triggerGroup);
            return new QuartzApiResult<>(0, "", quartzJobInfo);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
            return new QuartzApiResult<>(500, e.getMessage());
        }
    }

    @PostMapping("/job")
    public QuartzApiResult<Boolean> updateJob(@RequestBody JobUniqueId jobUniqueId) {
        try {
            quartzService.updateJob(jobUniqueId);
            return new QuartzApiResult<>(0, "", true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new QuartzApiResult<>(500, e.getMessage(), false);
        }
    }

    @DeleteMapping("/job/{jobName}/{jobGroup}")
    public QuartzApiResult<Boolean> deleteJob(@PathVariable String jobName,
                                              @PathVariable String jobGroup) {
        try {
            quartzService.deleteJob(jobName, jobGroup);
            return new QuartzApiResult<>(0, "", true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new QuartzApiResult<>(500, e.getMessage(), false);
        }
    }

    @PostMapping("/job/execute/{jobName}/{jobGroup}")
    public QuartzApiResult<Boolean> triggerJob(@PathVariable String jobName,
                                               @PathVariable String jobGroup) {
        try {
            quartzService.triggerJob(jobName, jobGroup);
            return new QuartzApiResult<>(0, "", true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new QuartzApiResult<>(500, e.getMessage(), false);
        }
    }

    @PostMapping("/job/pause/{jobName}/{jobGroup}")
    public QuartzApiResult<Boolean> pauseJob(@PathVariable String jobName,
                                             @PathVariable String jobGroup) {
        try {
            quartzService.pauseJob(jobName, jobGroup);
            return new QuartzApiResult<>(0, "", true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new QuartzApiResult<>(500, e.getMessage(), false);
        }
    }

    @PostMapping("/job/resume/{jobName}/{jobGroup}")
    public QuartzApiResult<Boolean> resumeJob(@PathVariable String jobName,
                                              @PathVariable String jobGroup) {
        try {
            quartzService.resumeJob(jobName, jobGroup);
            return new QuartzApiResult<>(0, "", true);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new QuartzApiResult<>(500, e.getMessage(), false);
        }
    }

}
