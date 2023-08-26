package redcoder.quartzplus.core.core.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QuartzSchedulerInstance {
    /**
     * the name of scheduler
     */
    private String schedName;

    /**
     * 实例主机地址
     */
    private String instanceHost;

    /**
     * 实例服务端口
     */
    private Integer instancePort;

    public QuartzSchedulerInstance(String schedName, String instanceHost, Integer instancePort) {
        this.schedName = schedName;
        this.instanceHost = instanceHost;
        this.instancePort = instancePort;
    }
}