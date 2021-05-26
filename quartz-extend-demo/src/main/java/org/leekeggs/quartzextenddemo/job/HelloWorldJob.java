package org.leekeggs.quartzextenddemo.job;

import org.leekeggs.quartzextendcore.annotation.QuartzJob;
import org.leekeggs.quartzextendcore.annotation.QuartzTrigger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author wuxiaoyuan
 * @since 2021-05-26
 */
@QuartzJob(jobDescription = "在控制台打印hello world")
@QuartzTrigger(triggerDescription = "HelloWorldJob's trigger", cronExpress = "0 0/1 * * * ?")
public class HelloWorldJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("hello world");
    }
}
