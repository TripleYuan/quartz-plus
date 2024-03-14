package redcoder.quartzplus.common.utils;

import org.apache.http.HttpException;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryException;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.backoff.NoBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;
import redcoder.quartzplus.common.exception.HttpExecutionFailException;

import java.net.ProtocolException;
import java.net.UnknownHostException;

/**
 * 使用Spring retry库，提供操作重试的能力
 */
public class SpringRetryUtils {

    /**
     * 使用默认的{@link RetryTemplate}执行指定的操作，如果执行失败，自动发起重试，直到执行成功或者遇到不允许继续重试的情况。
     * 默认的重试策略配置如下：
     * <ul>
     *     <li>最多重试2次</li>
     *     <li>{@link BackOffPolicy}策略是{@link NoBackOffPolicy}</li>
     *     <li>不可重试的异常：{@link UnknownHostException}, {@link ProtocolException}, {@link HttpException}, {@link HttpExecutionFailException}</li>
     *     <li>traverseCauses: true (see also: {@link BinaryExceptionClassifier#traverseCauses})</li>
     * </ul>
     *
     * @param retryCallback 待执行的操作
     * @param <T>           执行成功后，返回的结果
     * @param <E>           执行失败时，可能抛出的异常
     * @return 执行结果
     */
    public static <T, E extends Throwable> T doWithRetry(RetryCallback<T, E> retryCallback) {
        return doWithRetry(retryCallback, getDefaultRetryTemplate());
    }

    /**
     * 使用指定的{@link RetryTemplate}执行可重试的操作，
     * 如果执行失败，自动发起重试，直到执行成功或者遇到不允许继续重试的情况。
     *
     * @param retryCallback 待执行的操作
     * @param retryTemplate 用于执行操作的{@link RetryTemplate}
     * @param <T>           执行成功后，返回的结果
     * @param <E>           执行失败时，可能抛出的异常
     * @return 执行结果
     */
    public static <T, E extends Throwable> T doWithRetry(RetryCallback<T, E> retryCallback, RetryTemplate retryTemplate) {
        Assert.notNull(retryCallback, "'retryCallback' must not be null");
        Assert.notNull(retryTemplate, "'retryTemplate' must not be null");
        try {
            return retryTemplate.execute(retryCallback);
        } catch (Throwable e) {
            throw new RetryException("doWithRetry failed", e);
        }
    }

    /**
     * 获取用于执行操作{@link RetryCallback}的{@link RetryTemplate}，默认的重试策略配置如下：
     * <ul>
     *     <li>最多重试3次</li>
     *     <li>{@link BackOffPolicy}策略是{@link NoBackOffPolicy}</li>
     *     <li>不可重试的异常：{@link UnknownHostException}, {@link ProtocolException}, {@link HttpException}, {@link HttpExecutionFailException}</li>
     *     <li>traverseCauses: true (see also: {@link BinaryExceptionClassifier#traverseCauses})</li>
     * </ul>
     */
    public static RetryTemplate getDefaultRetryTemplate() {
        return RetryTemplate.builder()
                .maxAttempts(3)
                .noBackoff()
                .notRetryOn(UnknownHostException.class)
                .notRetryOn(ProtocolException.class)
                .notRetryOn(HttpException.class)
                .notRetryOn(HttpExecutionFailException.class)
                .traversingCauses()
                .build();
    }
}
