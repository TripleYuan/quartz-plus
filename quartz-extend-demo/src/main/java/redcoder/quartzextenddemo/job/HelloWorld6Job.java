package redcoder.quartzextenddemo.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import redcoder.quartzextendcore.annotation.QuartzJob;
import redcoder.quartzextendcore.annotation.QuartzTrigger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author redcoder54
 * @since 2021-05-26
 */
@QuartzJob(jobDescription = "在控制台打印hello world 6, 每隔5分钟执行一次")
@QuartzTrigger(cron = "0 0/5 * * * ?")
public class HelloWorld6Job implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("hello world 6, current time: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
