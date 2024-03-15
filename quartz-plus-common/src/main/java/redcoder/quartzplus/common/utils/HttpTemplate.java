package redcoder.quartzplus.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.client.config.RequestConfig;
import org.springframework.lang.Nullable;
import org.springframework.retry.support.RetryTemplate;
import redcoder.quartzplus.common.exception.HttpExecutionFailException;
import redcoder.quartzplus.common.exception.WrappedIOException;

import java.util.Map;

/**
 * http工具类，支持http get和post请求
 *
 * @since 1.0.0
 */
public class HttpTemplate {

    private HttpTemplate() {
    }

    /**
     * get请求
     * <ul>
     *     <li>HttpUtils会输出表示请求信息和返回信息的info日志，如果想禁用这类info日志，可调用{@link HttpUtils#disableNextHttpRequestInfoLog()}
     *     方法，禁止输出下一次http请求的info日志。</li>
     *     <li>如果http请求失败，HttpUtils输出包含错误信息的error日志，如果想禁用这类error日志，可调用{@link HttpUtils#disableHttpRequestFailErrorLog()}
     *     方法，禁止输出下一次http请求的error日志。</li>
     *     <li>HttpUtils支持开启重试机制，如果想使用该功能，可调用{@link HttpUtils#enableRetry()}，开启失败重试功能，如果不想使用
     *     默认的重试策略，可通过{@link HttpUtils#setRetryTemplate(RetryTemplate)}设置重试策略。注意：失败重试仅应用于下一次
     *     http请求。</li>
     * </ul>
     *
     * @param url         请求地址
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         包装IO异常，将checked io exception转换为 unchecked exception
     */
    public static <T> T doGet(String url, TypeReference<T> typeReference) {
        return doGet(url, null, typeReference);
    }

    /**
     * get请求
     * <ul>
     *     <li>HttpUtils会输出表示请求信息和返回信息的info日志，如果想禁用这类info日志，可调用{@link HttpUtils#disableNextHttpRequestInfoLog()}
     *     方法，禁止输出下一次http请求的info日志。</li>
     *     <li>如果http请求失败，HttpUtils输出包含错误信息的error日志，如果想禁用这类error日志，可调用{@link HttpUtils#disableHttpRequestFailErrorLog()}
     *     方法，禁止输出下一次http请求的error日志。</li>
     *     <li>HttpUtils支持开启重试机制，如果想使用该功能，可调用{@link HttpUtils#enableRetry()}，开启失败重试功能，如果不想使用
     *     默认的重试策略，可通过{@link HttpUtils#setRetryTemplate(RetryTemplate)}设置重试策略。注意：失败重试仅应用于下一次
     *     http请求。</li>
     * </ul>
     *
     * @param url         请求地址
     * @param queryParams 请求参数
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         包装IO异常，将checked io exception转换为 unchecked exception
     */
    public static <T> T doGet(String url, Map<String, String> queryParams, TypeReference<T> typeReference) {
        return doGet(url, queryParams, null, typeReference);
    }

    /**
     * get请求
     * <ul>
     *     <li>HttpUtils会输出表示请求信息和返回信息的info日志，如果想禁用这类info日志，可调用{@link HttpUtils#disableNextHttpRequestInfoLog()}
     *     方法，禁止输出下一次http请求的info日志。</li>
     *     <li>如果http请求失败，HttpUtils输出包含错误信息的error日志，如果想禁用这类error日志，可调用{@link HttpUtils#disableHttpRequestFailErrorLog()}
     *     方法，禁止输出下一次http请求的error日志。</li>
     *     <li>HttpUtils支持开启重试机制，如果想使用该功能，可调用{@link HttpUtils#enableRetry()}，开启失败重试功能，如果不想使用
     *     默认的重试策略，可通过{@link HttpUtils#setRetryTemplate(RetryTemplate)}设置重试策略。注意：失败重试仅应用于下一次
     *     http请求。</li>
     * </ul>
     *
     * @param url         请求地址
     * @param queryParams 请求参数
     * @param headers     header头信息
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         包装IO异常，将checked io exception转换为 unchecked exception
     */
    public static <T> T doGet(String url,
                              @Nullable Map<String, String> queryParams,
                              @Nullable Map<String, String> headers,
                              TypeReference<T> typeReference) {
        String response = HttpUtils.doGet(url, queryParams, headers);
        return JsonUtils.toBean(response, typeReference);
    }

    /**
     * post请求，Content-Type：application/x-www-form-urlencoded
     * <ul>
     *     <li>HttpUtils会输出表示请求信息和返回信息的info日志，如果想禁用这类info日志，可调用{@link HttpUtils#disableNextHttpRequestInfoLog()}
     *     方法，禁止输出下一次http请求的info日志。</li>
     *     <li>如果http请求失败，HttpUtils输出包含错误信息的error日志，如果想禁用这类error日志，可调用{@link HttpUtils#disableHttpRequestFailErrorLog()}
     *     方法，禁止输出下一次http请求的error日志。</li>
     *     <li>HttpUtils支持开启重试机制，如果想使用该功能，可调用{@link HttpUtils#enableRetry()}，开启失败重试功能，如果不想使用
     *     默认的重试策略，可通过{@link HttpUtils#setRetryTemplate(RetryTemplate)}设置重试策略。注意：失败重试仅应用于下一次
     *     http请求。</li>
     * </ul>
     *
     * @param url        请求地址
     * @param formParams 表单参数
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         包装IO异常，将checked io exception转换为 unchecked exception
     */
    public static <T> T doPost(String url, Map<String, String> formParams, TypeReference<T> typeReference) {
        return doPost(url, "application/x-www-form-urlencoded", null, formParams, null, typeReference);
    }

    /**
     * post请求，Content-Type：application/x-www-form-urlencoded
     * <ul>
     *     <li>HttpUtils会输出表示请求信息和返回信息的info日志，如果想禁用这类info日志，可调用{@link HttpUtils#disableNextHttpRequestInfoLog()}
     *     方法，禁止输出下一次http请求的info日志。</li>
     *     <li>如果http请求失败，HttpUtils输出包含错误信息的error日志，如果想禁用这类error日志，可调用{@link HttpUtils#disableHttpRequestFailErrorLog()}
     *     方法，禁止输出下一次http请求的error日志。</li>
     *     <li>HttpUtils支持开启重试机制，如果想使用该功能，可调用{@link HttpUtils#enableRetry()}，开启失败重试功能，如果不想使用
     *     默认的重试策略，可通过{@link HttpUtils#setRetryTemplate(RetryTemplate)}设置重试策略。注意：失败重试仅应用于下一次
     *     http请求。</li>
     * </ul>
     *
     * @param url        请求地址
     * @param headers    header信息
     * @param formParams 表单参数
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         包装IO异常，将checked io exception转换为 unchecked exception
     */
    public static <T> T doPost(String url, Map<String, String> headers, Map<String, String> formParams, TypeReference<T> typeReference) {
        return doPost(url, "application/x-www-form-urlencoded", headers, formParams, null, typeReference);
    }

    /**
     * post请求，Content-Type：application/json
     * <ul>
     *     <li>HttpUtils会输出表示请求信息和返回信息的info日志，如果想禁用这类info日志，可调用{@link HttpUtils#disableNextHttpRequestInfoLog()}
     *     方法，禁止输出下一次http请求的info日志。</li>
     *     <li>如果http请求失败，HttpUtils输出包含错误信息的error日志，如果想禁用这类error日志，可调用{@link HttpUtils#disableHttpRequestFailErrorLog()}
     *     方法，禁止输出下一次http请求的error日志。</li>
     *     <li>HttpUtils支持开启重试机制，如果想使用该功能，可调用{@link HttpUtils#enableRetry()}，开启失败重试功能，如果不想使用
     *     默认的重试策略，可通过{@link HttpUtils#setRetryTemplate(RetryTemplate)}设置重试策略。注意：失败重试仅应用于下一次
     *     http请求。</li>
     * </ul>
     *
     * @param url         请求地址
     * @param requestBody 请求体
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         包装IO异常，将checked io exception转换为 unchecked exception
     */
    public static <T> T doPost(String url, String requestBody, TypeReference<T> typeReference) {
        return doPost(url, "application/json", null, null, requestBody, typeReference);
    }

    /**
     * post请求，Content-Type：application/json
     * <ul>
     *     <li>HttpUtils会输出表示请求信息和返回信息的info日志，如果想禁用这类info日志，可调用{@link HttpUtils#disableNextHttpRequestInfoLog()}
     *     方法，禁止输出下一次http请求的info日志。</li>
     *     <li>如果http请求失败，HttpUtils输出包含错误信息的error日志，如果想禁用这类error日志，可调用{@link HttpUtils#disableHttpRequestFailErrorLog()}
     *     方法，禁止输出下一次http请求的error日志。</li>
     *     <li>HttpUtils支持开启重试机制，如果想使用该功能，可调用{@link HttpUtils#enableRetry()}，开启失败重试功能，如果不想使用
     *     默认的重试策略，可通过{@link HttpUtils#setRetryTemplate(RetryTemplate)}设置重试策略。注意：失败重试仅应用于下一次
     *     http请求。</li>
     * </ul>
     *
     * @param url         请求地址
     * @param headers     header信息
     * @param requestBody 请求体
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         包装IO异常，将checked io exception转换为 unchecked exception
     */
    public static <T> T doPost(String url, Map<String, String> headers, String requestBody, TypeReference<T> typeReference) {
        return doPost(url, "application/json", headers, null, requestBody, typeReference);
    }

    /**
     * post请求。
     * <ul>
     *     <li>HttpUtils会输出表示请求信息和返回信息的info日志，如果想禁用这类info日志，可调用{@link HttpUtils#disableNextHttpRequestInfoLog()}
     *     方法，禁止输出下一次http请求的info日志。</li>
     *     <li>如果http请求失败，HttpUtils输出包含错误信息的error日志，如果想禁用这类error日志，可调用{@link HttpUtils#disableHttpRequestFailErrorLog()}
     *     方法，禁止输出下一次http请求的error日志。</li>
     *     <li>HttpUtils支持开启重试机制，如果想使用该功能，可调用{@link HttpUtils#enableRetry()}，开启失败重试功能，如果不想使用
     *     默认的重试策略，可通过{@link HttpUtils#setRetryTemplate(RetryTemplate)}设置重试策略。注意：失败重试仅应用于下一次
     *     http请求。</li>
     * </ul>
     *
     * @param url         请求地址
     * @param contentType Content-Type
     * @param headers     header信息
     * @param formParams  请求参数
     * @param requestBody 请求体
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         包装IO异常，将checked io exception转换为 unchecked exception
     */
    public static <T> T doPost(String url,
                               String contentType,
                               @Nullable Map<String, String> headers,
                               @Nullable Map<String, String> formParams,
                               @Nullable String requestBody,
                               TypeReference<T> typeReference) {
        String response = HttpUtils.doPost(url, contentType, headers, formParams, requestBody);
        return JsonUtils.toBean(response, typeReference);
    }

    /**
     * delete请求
     * <ul>
     *     <li>HttpUtils会输出表示请求信息和返回信息的info日志，如果想禁用这类info日志，可调用{@link HttpUtils#disableNextHttpRequestInfoLog()}
     *     方法，禁止输出下一次http请求的info日志。</li>
     *     <li>如果http请求失败，HttpUtils输出包含错误信息的error日志，如果想禁用这类error日志，可调用{@link HttpUtils#disableHttpRequestFailErrorLog()}
     *     方法，禁止输出下一次http请求的error日志。</li>
     *     <li>HttpUtils支持开启重试机制，如果想使用该功能，可调用{@link HttpUtils#enableRetry()}，开启失败重试功能，如果不想使用
     *     默认的重试策略，可通过{@link HttpUtils#setRetryTemplate(RetryTemplate)}设置重试策略。注意：失败重试仅应用于下一次
     *     http请求。</li>
     * </ul>
     *
     * @param url 请求地址
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         包装IO异常，将checked io exception转换为 unchecked exception
     */
    public static <T> T doDelete(String url, TypeReference<T> typeReference) {
        String response = HttpUtils.doDelete(url);
        return JsonUtils.toBean(response, typeReference);
    }

    /**
     * 传输文件，支持<code>File</code>，<code>InputStream</code>，<code>byte[]</code>，<code>String</code>
     *
     * @param url        url
     * @param formParams 表单参数，key-参数名称，value-参数值（支持<code>File</code>，<code>InputStream</code>，
     *                   <code>byte[]</code>，<code>String</code>）
     * @param headers    header，可选参数
     * @return 响应体内容
     */
    public static <T> T transferFiles(String url,
                                      Map<String, Object> formParams,
                                      @Nullable Map<String, String> headers,
                                      TypeReference<T> typeReference) {
        String response = HttpUtils.transferFiles(url, formParams, headers);
        return JsonUtils.toBean(response, typeReference);
    }
}
