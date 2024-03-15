package redcoder.quartzplus.schedcenter.entity.key;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuartzPlusJobInfoKey implements Serializable {

    /**
     * the name of scheduler
     */
    private String schedName;

    private String jobName;

    private String jobGroup;

    public QuartzPlusJobInfoKey() {
    }

    public QuartzPlusJobInfoKey(String schedName, String jobName, String jobGroup) {
        this.schedName = schedName;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
    }
}
