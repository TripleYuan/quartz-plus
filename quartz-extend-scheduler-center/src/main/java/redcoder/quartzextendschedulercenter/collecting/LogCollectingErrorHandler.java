package redcoder.quartzextendschedulercenter.collecting;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import redcoder.quartzextendcommon.utils.JsonUtils;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerInstance;

/**
 * 采集出错时，打印错误日志
 *
 * @author wuxiaoyuan
 */
@Slf4j
@Component
public class LogCollectingErrorHandler implements CollectingErrorHandler {

    @Override
    public boolean handle(QuartzSchedulerInstance instance, Exception exception) {
        log.error("收集数据失败: " + JsonUtils.beanToJsonString(instance), exception);
        return true;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
