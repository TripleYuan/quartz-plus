package redcoder.quartzplus.core.scheduler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * quartz任务调度管理平台的配置属性
 *
 * @author redcoder
 * @since 1.4
 */
@ConfigurationProperties(prefix = "quartz-job-scheduler.registry")
@Setter
@Getter
public class QuartzJobSchedulerProperties {

    /**
     * 注册实例信息的地址
     */
    private String registerUrl;

    /**
     * 解除注册实例信息的地址
     */
    private String unregisterUrl;

    /**
     * 任务执行记录上报地址
     */
    private String reportUrl;

    /**
     * 如果为true，Job类只会创建一个实例，否则每次运行job时都会创建一个实例，默认为true.
     */
    private boolean singletonJob = true;

}
