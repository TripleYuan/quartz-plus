package redcoder.quartzplus.schedcenter.entity.key;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuartzSchedulerInstanceKey implements Serializable {

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

    public QuartzSchedulerInstanceKey() {
    }

    public QuartzSchedulerInstanceKey(String schedName, String instanceHost, Integer instancePort) {
        this.schedName = schedName;
        this.instanceHost = instanceHost;
        this.instancePort = instancePort;
    }
}
