package redcoder.quartzplus.schedcenter.collect;

import org.springframework.core.Ordered;
import redcoder.quartzplus.schedcenter.entity.QuartzPlusInstance;

/**
 * 采集错误处理器：采集出错的情况下，如何处理出错的quartz实例信息。
 * 多个采集错误处理器可构成一个错误处理器链。
 *
 * @author redcoder54
 */
public interface CollectingErrorHandler extends Ordered {

    @Override
    default int getOrder() {
        return 0;
    }

    /**
     * 处理采集错误
     *
     * @param instance  出错的实例信息
     * @param exception 异常
     * @return 如果返回true表示采集错误已经处理完成，不需要传递给后续的处理器了。
     */
    boolean handle(QuartzPlusInstance instance, Exception exception);
}
