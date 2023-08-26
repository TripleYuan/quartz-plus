package redcoder.quartzplus.schedcenter.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;
import redcoder.quartzplus.core.annotation.QuartzJob;
import redcoder.quartzplus.core.annotation.QuartzTrigger;
import redcoder.quartzplus.schedcenter.collect.JobTriggerInfoCollector;

import javax.annotation.Resource;

/**
 * 采集quartz job和trigger信息，保存到数据库表中
 *
 * @author redcoder54
 * @since 2021-04-25
 */
@Slf4j
@QuartzJob(description = "采集quartz实例上的job和trigger信息")
@QuartzTrigger(description = "JobTriggerInfoCollectingJob's trigger", cron = "0 0/5 * * * ?")
public class JobTriggerInfoCollectingJob extends QuartzJobBean {

    @Resource
    private JobTriggerInfoCollector collector;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        try {
            collector.collect();
        } catch (Exception e) {
            log.error("JobTriggerInfoCollectingJob error", e);
        }
    }
}
