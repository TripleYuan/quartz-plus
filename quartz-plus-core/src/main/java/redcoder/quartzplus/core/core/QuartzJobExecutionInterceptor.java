package redcoder.quartzplus.core.core;


import com.fasterxml.jackson.core.type.TypeReference;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import redcoder.quartzplus.common.utils.HttpTemplate;
import redcoder.quartzplus.common.utils.JsonUtils;
import redcoder.quartzplus.core.core.dto.JobExecutionRecordDto;
import redcoder.quartzplus.core.core.dto.QuartzApiResult;
import redcoder.quartzplus.core.scheduler.QuartzJobSchedulerProperties;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class QuartzJobExecutionInterceptor implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(QuartzJobExecutionInterceptor.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Environment environment;
    private QuartzJobSchedulerProperties properties;

    public QuartzJobExecutionInterceptor(Environment environment, QuartzJobSchedulerProperties properties) {
        this.environment = environment;
        this.properties = properties;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object[] arguments = invocation.getArguments();
        if (arguments.length == 0 || !(arguments[0] instanceof JobExecutionContext)) {
            return invocation.proceed();
        }

        JobExecutionContext context = (JobExecutionContext) arguments[0];
        LocalDateTime startTime = LocalDateTime.now();
        long start = System.nanoTime();

        Object result = null;
        Throwable throwable = null;
        try {
            result = invocation.proceed();
        } catch (Throwable e) {
            throwable = e;
        }

        long end = System.nanoTime();
        LocalDateTime endTime = LocalDateTime.now();
        long costTime = TimeUnit.NANOSECONDS.toMillis(end - start);

        try {
            report(context, startTime, endTime, costTime, 2, throwable == null ? null : getStackTrace(throwable));
        } catch (Throwable e) {
            log.warn("report error", e);
        }

        if (throwable == null) {
            return result;
        } else {
            throw throwable;
        }
    }

    private void report(JobExecutionContext context,
                        LocalDateTime startTime,
                        LocalDateTime endTime,
                        long costTime,
                        int status,
                        String exception) throws SchedulerException {
        String reportUrl = properties.getReportUrl();
        if (reportUrl == null) {
            log.warn("Cannot report: report url not found");
            return;
        }

        JobExecutionRecordDto dto = new JobExecutionRecordDto();
        dto.setSchedName(context.getScheduler().getSchedulerName());
        dto.setJobName(context.getJobDetail().getKey().getName());
        dto.setJobGroup(context.getJobDetail().getKey().getGroup());
        dto.setStatus(status);
        dto.setStartTime(startTime.format(DATE_TIME_FORMATTER));
        dto.setEndTime(endTime.format(DATE_TIME_FORMATTER));
        dto.setCostTime(costTime);
        dto.setStatus(status);
        dto.setException(exception);
        QuartzApiResult<String> result = HttpTemplate.doPost(reportUrl, JsonUtils.toJsonString(dto),
                new TypeReference<QuartzApiResult<String>>() {
                });
        if (result.getStatus() != 0) {
            log.warn("report failure, status: {}, message: {}", result.getStatus(), result.getMessage());
        }
    }

    private String getStackTrace(final Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }
}
