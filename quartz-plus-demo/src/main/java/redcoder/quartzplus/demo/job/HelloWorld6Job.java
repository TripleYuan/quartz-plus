package redcoder.quartzplus.demo.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import redcoder.quartzplus.core.annotation.QuartzJob;
import redcoder.quartzplus.core.annotation.QuartzTrigger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@QuartzJob(description = "在控制台打印hello world 6, 每隔5分钟执行一次")
@QuartzTrigger(cron = "0 0/5 * * * ?")
public class HelloWorld6Job implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("hello world 6, current time: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
