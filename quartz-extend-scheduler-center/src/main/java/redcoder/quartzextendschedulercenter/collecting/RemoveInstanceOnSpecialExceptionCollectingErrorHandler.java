package redcoder.quartzextendschedulercenter.collecting;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.HttpHostConnectException;
import org.springframework.stereotype.Component;
import redcoder.quartzextendcommon.exception.WrappedIOException;
import redcoder.quartzextendschedulercenter.entity.QuartzSchedulerInstance;
import redcoder.quartzextendschedulercenter.entity.key.QuartzSchedulerInstanceKey;
import redcoder.quartzextendschedulercenter.repository.InstanceRepository;

import javax.annotation.Resource;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

/**
 * 采集出错时，如果异常类在指定范围内，将删除出错的实例信息
 *
 * @author wuxiaoyuan
 */
@Component
@Slf4j
public class RemoveInstanceOnSpecialExceptionCollectingErrorHandler implements CollectingErrorHandler {

    private final List<Class<? extends Exception>> exceptions = Arrays.asList(UnknownHostException.class, HttpHostConnectException.class);

    @Resource
    private InstanceRepository instanceRepository;

    @Override
    public boolean handle(QuartzSchedulerInstance instance, Exception exception) {
        for (Class<? extends Exception> exClz : exceptions) {
            if (exception.getClass().isAssignableFrom(exClz)
                    || (exception.getClass().isAssignableFrom(WrappedIOException.class) && exception.getCause().getClass().isAssignableFrom(exClz))) {
                deleteInstance(instance, exception);
                return true;
            }
        }
        return false;
    }

    private void deleteInstance(QuartzSchedulerInstance instance, Exception exception) {
        QuartzSchedulerInstanceKey key = new QuartzSchedulerInstanceKey(instance.getSchedName(),
                instance.getInstanceHost(), instance.getInstancePort());
        instanceRepository.deleteById(key);
        log.info("采集信息失败，已删除实例信息. [schedName = {}, host = {}, port = {}]",
                instance.getSchedName(), instance.getInstanceHost(), instance.getInstancePort(), exception);
    }
}
