package org.leekeggs.quartzextendcommon.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.client.config.RequestConfig;
import org.leekeggs.quartzextendcommon.exception.HttpExecutionFailException;
import org.leekeggs.quartzextendcommon.exception.WrappedIOException;
import org.springframework.lang.Nullable;

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
     * <p>
     * 如果需要修改http RequestConfig, 可通过{@link HttpTemplate#setRequestConfig(RequestConfig)}方法，添加配置，
     * 在执行http请求前，会自动应用该配置，并在请求结束后，移除该配置，不影响后续的请求。
     * <p>
     * 注意：如果调用{@link HttpTemplate#setRequestConfig(RequestConfig)}方法，添加了配置，但却没有执行http请求，
     * 请务必删除调用{@link HttpTemplate#removeRequestConfig()}方法，移除配置，避免影响下一次http请求
     *
     * @param url 请求地址
     * @param <T> 返回类型
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         封装的io异常，将checked io exception转换为 unchecked exception
     */
    public static <T> T doGet(String url, TypeReference<T> typeReference) {
        return doGet(url, null, typeReference);
    }

    /**
     * get请求
     * <p>
     * 如果需要修改http RequestConfig, 可通过{@link HttpTemplate#setRequestConfig(RequestConfig)}方法，添加配置，
     * 在执行http请求前，会自动应用该配置，并在请求结束后，移除该配置，不影响后续的请求。
     * <p>
     * 注意：如果调用{@link HttpTemplate#setRequestConfig(RequestConfig)}方法，添加了配置，但却没有执行http请求，
     * 请务必删除调用{@link HttpTemplate#removeRequestConfig()}方法，移除配置，避免影响下一次http请求
     *
     * @param url         请求地址
     * @param queryParams 请求参数
     * @param <T>         返回类型
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         封装的io异常，将checked io exception转换为 unchecked exception
     */
    public static <T> T doGet(String url, Map<String, String> queryParams, TypeReference<T> typeReference) {
        return doGet(url, queryParams, null, typeReference);
    }

    /**
     * get请求
     * <p>
     * 如果需要修改http RequestConfig, 可通过{@link HttpTemplate#setRequestConfig(RequestConfig)}方法，添加配置，
     * 在执行http请求前，会自动应用该配置，并在请求结束后，移除该配置，不影响后续的请求。
     * <p>
     * 注意：如果调用{@link HttpTemplate#setRequestConfig(RequestConfig)}方法，添加了配置，但却没有执行http请求，
     * 请务必删除调用{@link HttpTemplate#removeRequestConfig()}方法，移除配置，避免影响下一次http请求
     *
     * @param url         请求地址
     * @param queryParams 请求参数
     * @param headers     header头信息
     * @param <T>         返回类型
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         封装的io异常，将checked io exception转换为 unchecked exception
     */
    public static <T> T doGet(String url,
                              @Nullable Map<String, String> queryParams,
                              @Nullable Map<String, String> headers,
                              TypeReference<T> typeReference) {
        String response = HttpUtils.doGet(url, queryParams, headers);
        return JsonUtils.jsonStringToBean(response, typeReference);
    }

    /**
     * post请求，Content-Type：application/x-www-form-urlencoded
     * <p>
     * 如果需要修改http RequestConfig, 可通过{@link HttpTemplate#setRequestConfig(RequestConfig)}方法，添加配置，
     * 在执行http请求前，会自动应用该配置，并在请求结束后，移除该配置，不影响后续的请求。
     * <p>
     * 注意：如果调用{@link HttpTemplate#setRequestConfig(RequestConfig)}方法，添加了配置，但却没有执行http请求，
     * 请务必删除调用{@link HttpTemplate#removeRequestConfig()}方法，移除配置，避免影响下一次http请求
     *
     * @param url        请求地址
     * @param formParams 表单参数
     * @param <T>        返回类型
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         封装的io异常，将checked io exception转换为 unchecked exception
     */
    public static <T> T doPost(String url, Map<String, String> formParams, TypeReference<T> typeReference) {
        return doPost(url, "application/x-www-form-urlencoded", null, formParams, null, typeReference);
    }

    /**
     * post请求，Content-Type：application/x-www-form-urlencoded
     * <p>
     * 如果需要修改http RequestConfig, 可通过{@link HttpTemplate#setRequestConfig(RequestConfig)}方法，添加配置，
     * 在执行http请求前，会自动应用该配置，并在请求结束后，移除该配置，不影响后续的请求。
     * <p>
     * 注意：如果调用{@link HttpTemplate#setRequestConfig(RequestConfig)}方法，添加了配置，但却没有执行http请求，
     * 请务必删除调用{@link HttpTemplate#removeRequestConfig()}方法，移除配置，避免影响下一次http请求
     *
     * @param url        请求地址
     * @param headers    header信息
     * @param formParams 表单参数
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         封装的io异常，将checked io exception转换为 unchecked exception
     */
    public static <T> T doPost(String url, Map<String, String> headers, Map<String, String> formParams, TypeReference<T> typeReference) {
        return doPost(url, "application/x-www-form-urlencoded", headers, formParams, null, typeReference);
    }

    /**
     * post请求，Content-Type：application/json
     * <p>
     * 如果需要修改http RequestConfig, 可通过{@link HttpTemplate#setRequestConfig(RequestConfig)}方法，添加配置，
     * 在执行http请求前，会自动应用该配置，并在请求结束后，移除该配置，不影响后续的请求。
     * <p>
     * 注意：如果调用{@link HttpTemplate#setRequestConfig(RequestConfig)}方法，添加了配置，但却没有执行http请求，
     * 请务必删除调用{@link HttpTemplate#removeRequestConfig()}方法，移除配置，避免影响下一次http请求
     *
     * @param url         请求地址
     * @param requestBody 请求体
     * @param <T>         返回类型
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         封装的io异常，将checked io exception转换为 unchecked exception
     */
    public static <T> T doPost(String url, String requestBody, TypeReference<T> typeReference) {
        return doPost(url, "application/json", null, null, requestBody, typeReference);
    }

    /**
     * post请求，Content-Type：application/json
     * <p>
     * 如果需要修改http RequestConfig, 可通过{@link HttpTemplate#setRequestConfig(RequestConfig)}方法，添加配置，
     * 在执行http请求前，会自动应用该配置，并在请求结束后，移除该配置，不影响后续的请求。
     * <p>
     * 注意：如果调用{@link HttpTemplate#setRequestConfig(RequestConfig)}方法，添加了配置，但却没有执行http请求，
     * 请务必删除调用{@link HttpTemplate#removeRequestConfig()}方法，移除配置，避免影响下一次http请求
     *
     * @param url         请求地址
     * @param headers     header信息
     * @param requestBody 请求体
     * @param <T>         返回类型
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         封装的io异常，将checked io exception转换为 unchecked exception
     */
    public static <T> T doPost(String url, Map<String, String> headers, String requestBody, TypeReference<T> typeReference) {
        return doPost(url, "application/json", headers, null, requestBody, typeReference);
    }

    /**
     * post请求。
     * <p>
     * 如果需要修改http RequestConfig, 可通过{@link HttpTemplate#setRequestConfig(RequestConfig)}方法，添加配置，
     * 在执行http请求前，会自动应用该配置，并在请求结束后，移除该配置，不影响后续的请求。
     * <p>
     * 注意：如果调用{@link HttpTemplate#setRequestConfig(RequestConfig)}方法，添加了配置，但却没有执行http请求，
     * 请务必删除调用{@link HttpTemplate#removeRequestConfig()}方法，移除配置，避免影响下一次http请求
     *
     * @param url         请求地址
     * @param contentType Content-Type
     * @param headers     header信息
     * @param formParams  请求参数
     * @param requestBody 请求体
     * @param <T>         返回类型
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         封装的io异常，将checked io exception转换为 unchecked exception
     */
    public static <T> T doPost(String url,
                               String contentType,
                               @Nullable Map<String, String> headers,
                               @Nullable Map<String, String> formParams,
                               @Nullable String requestBody,
                               TypeReference<T> typeReference) {
        String response = HttpUtils.doPost(url, contentType, headers, formParams, requestBody);
        return JsonUtils.jsonStringToBean(response, typeReference);
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
        return JsonUtils.jsonStringToBean(response, typeReference);
    }

    /**
     * 构建RequestConfig
     *
     * @param connectTimeoutMills           连接超时时间，单位毫秒
     * @param socketTimeoutMills            请求获取数据的超时时间，单位毫秒
     * @param connectionRequestTimeoutMills 从连接池中获取连接得超时时间
     * @return RequestConfig
     */
    public static RequestConfig buildRequestConfig(int connectTimeoutMills,
                                                   int socketTimeoutMills,
                                                   int connectionRequestTimeoutMills) {
        return HttpUtils.buildRequestConfig(connectTimeoutMills, socketTimeoutMills, connectionRequestTimeoutMills);
    }

    /**
     * 设置RequestConfig，与当前线程绑定
     *
     * @param config http配置信息
     */
    public static void setRequestConfig(RequestConfig config) {
        HttpUtils.setRequestConfig(config);
    }

    /**
     * 获取与当前线程绑定的RequestConfig，如果不存在，return null
     */
    @Nullable
    public static RequestConfig getRequestConfig() {
        return HttpUtils.getRequestConfig();
    }

    /**
     * 移除当前线程绑定的RequestConfig
     */
    public static void removeRequestConfig() {
        HttpUtils.removeRequestConfig();
    }
}
