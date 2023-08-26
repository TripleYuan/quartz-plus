package redcoder.quartzplus.schedcenter.collect;

/**
 * 负责采集job和trigger信息的采集器
 *
 * @author redcoder54
 */
public interface JobTriggerInfoCollector {

    void collect();

    void collect(String schedName, String host, int port);
}
