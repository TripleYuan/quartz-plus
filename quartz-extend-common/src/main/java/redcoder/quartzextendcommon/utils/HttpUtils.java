package redcoder.quartzextendcommon.utils;

import org.apache.http.Consts;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import redcoder.quartzextendcommon.exception.HttpExecutionFailException;
import redcoder.quartzextendcommon.exception.WrappedIOException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * http工具类，支持http get和post请求
 */
public class HttpUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    private static final CloseableHttpClient httpClient;
    private static final PoolingHttpClientConnectionManager connectionManager;

    /**
     * 连接超时时间，60s
     */
    private static final int CONNECTION_TIMEOUT = 1000 * 60;

    /**
     * 读取响应数据超时时间，60s
     */
    private static final int SOCKET_TIMEOUT = 1000 * 60;

    /**
     * 从连接池中获取连接得超时时间
     */
    private static final int CONNECTION_REQUEST_TIMEOUT = 1000 * 5;

    /**
     * 保存RequestConfig，发送http请求前使用，请求完成后（包括异常完成），清除RequestConfig
     */
    private static final ThreadLocal<RequestConfig> REQUEST_CONFIG_CONTEXT = new ThreadLocal<>();

    /**
     * 是否禁用error日志的标志，当http请求发生错误时，如果禁用标志为true，则使用warn日志代替error日志（请求结束后，会清除标志），
     * 默认false。
     */
    private static final ThreadLocal<Boolean> DISABLE_ERROR_LOG_MARK = ThreadLocal.withInitial(() -> false);

    static {
        // 配置连接管理器
        connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(1000);//设置最大连接数
        connectionManager.setDefaultMaxPerRoute(200);//设置单个路由最大的连接数

        httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager) // 连接管理器
                .setDefaultRequestConfig(buildRequestConfig(CONNECTION_TIMEOUT, SOCKET_TIMEOUT, CONNECTION_REQUEST_TIMEOUT)) // 超时配置
                .setConnectionReuseStrategy(DefaultClientConnectionReuseStrategy.INSTANCE) // 使用默认的连接重用策略
                .setKeepAliveStrategy((response, context) -> {  // 设置空闲连接存活时间策略
                    DefaultConnectionKeepAliveStrategy instance = DefaultConnectionKeepAliveStrategy.INSTANCE;
                    long duration = instance.getKeepAliveDuration(response, context);
                    if (duration <= 0) {
                        // 默认策略返回的值小于0，将时间设置为60s，避免空闲连接无限期存活
                        return 60 * 1000;
                    }
                    return duration;
                })
                .build();
    }

    private HttpUtils() {
    }

    public static CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public static PoolingHttpClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    /**
     * get请求
     * <p>
     * 如果需要修改http RequestConfig, 可通过{@link HttpUtils#setRequestConfig(RequestConfig)}方法，添加配置，
     * 在执行http请求前，会自动应用该配置，并在请求结束后，移除该配置，不影响后续的请求。
     * <p>
     * 注意：如果调用{@link HttpUtils#setRequestConfig(RequestConfig)}方法，添加了配置，但却没有执行http请求，
     * 请务必删除调用{@link HttpUtils#removeRequestConfig()}方法，移除配置，避免影响下一次http请求
     *
     * @param url 请求地址
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         封装的io异常，将checked io exception转换为 unchecked exception
     */
    public static String doGet(String url) {
        return doGet(url, null);
    }

    /**
     * get请求
     * <p>
     * 如果需要修改http RequestConfig, 可通过{@link HttpUtils#setRequestConfig(RequestConfig)}方法，添加配置，
     * 在执行http请求前，会自动应用该配置，并在请求结束后，移除该配置，不影响后续的请求。
     * <p>
     * 注意：如果调用{@link HttpUtils#setRequestConfig(RequestConfig)}方法，添加了配置，但却没有执行http请求，
     * 请务必删除调用{@link HttpUtils#removeRequestConfig()}方法，移除配置，避免影响下一次http请求
     *
     * @param url         请求地址
     * @param queryParams 请求参数
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         封装的io异常，将checked io exception转换为 unchecked exception
     */
    public static String doGet(String url, Map<String, String> queryParams) {
        return doGet(url, queryParams, null);
    }

    /**
     * get请求
     * <p>
     * 如果需要修改http RequestConfig, 可通过{@link HttpUtils#setRequestConfig(RequestConfig)}方法，添加配置，
     * 在执行http请求前，会自动应用该配置，并在请求结束后，移除该配置，不影响后续的请求。
     * <p>
     * 注意：如果调用{@link HttpUtils#setRequestConfig(RequestConfig)}方法，添加了配置，但却没有执行http请求，
     * 请务必删除调用{@link HttpUtils#removeRequestConfig()}方法，移除配置，避免影响下一次http请求
     *
     * @param url         请求地址
     * @param queryParams 请求参数
     * @param headers     header头信息
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         封装的io异常，将checked io exception转换为 unchecked exception
     */
    public static String doGet(String url,
                               @Nullable Map<String, String> queryParams,
                               @Nullable Map<String, String> headers) {
        LogContext logContext = LogContext.createInstance(true, Level.INFO).logger(log);
        ;
        try {
            logContext.append("http get请求，", url);
            Optional.ofNullable(queryParams).ifPresent(map -> logContext.append("queryParams: ", queryParams));
            Optional.ofNullable(headers).ifPresent(map -> logContext.append("headers: ", headers));

            // http httpGet
            HttpGet httpGet;
            try {
                URIBuilder uriBuilder = new URIBuilder(url);
                // 设置参数
                if (queryParams != null && !queryParams.isEmpty()) {
                    queryParams.forEach(uriBuilder::setParameter);
                }
                // 创建httpGet
                httpGet = new HttpGet(uriBuilder.build());
            } catch (URISyntaxException e) {
                log.error("无效的uri, " + url, e);
                logContext.append(Level.ERROR, "error: 无效的uri, " + url);
                throw new IllegalArgumentException("无效的uri, " + url);
            }
            // 添加header
            if (headers != null && !headers.isEmpty()) {
                headers.forEach(httpGet::setHeader);
            }
            // set RequestConfig
            Optional.ofNullable(getRequestConfig()).ifPresent(httpGet::setConfig);

            return executeHttpRequest(httpGet, logContext);
        } finally {
            logContext.print(false);
            removeRequestConfig();
            DISABLE_ERROR_LOG_MARK.remove();
        }
    }

    /**
     * post请求，Content-Type：application/x-www-form-urlencoded
     * <p>
     * 如果需要修改http RequestConfig, 可通过{@link HttpUtils#setRequestConfig(RequestConfig)}方法，添加配置，
     * 在执行http请求前，会自动应用该配置，并在请求结束后，移除该配置，不影响后续的请求。
     * <p>
     * 注意：如果调用{@link HttpUtils#setRequestConfig(RequestConfig)}方法，添加了配置，但却没有执行http请求，
     * 请务必删除调用{@link HttpUtils#removeRequestConfig()}方法，移除配置，避免影响下一次http请求
     *
     * @param url        请求地址
     * @param formParams 表单参数
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         封装的io异常，将checked io exception转换为 unchecked exception
     */
    public static String doPost(String url, Map<String, String> formParams) {
        return doPost(url, "application/x-www-form-urlencoded", null, formParams, null);
    }

    /**
     * post请求，Content-Type：application/x-www-form-urlencoded
     * <p>
     * 如果需要修改http RequestConfig, 可通过{@link HttpUtils#setRequestConfig(RequestConfig)}方法，添加配置，
     * 在执行http请求前，会自动应用该配置，并在请求结束后，移除该配置，不影响后续的请求。
     * <p>
     * 注意：如果调用{@link HttpUtils#setRequestConfig(RequestConfig)}方法，添加了配置，但却没有执行http请求，
     * 请务必删除调用{@link HttpUtils#removeRequestConfig()}方法，移除配置，避免影响下一次http请求
     *
     * @param url        请求地址
     * @param headers    header信息
     * @param formParams 表单参数
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         封装的io异常，将checked io exception转换为 unchecked exception
     */
    public static String doPost(String url, Map<String, String> headers, Map<String, String> formParams) {
        return doPost(url, "application/x-www-form-urlencoded", headers, formParams, null);
    }

    /**
     * post请求，Content-Type：application/json
     * <p>
     * 如果需要修改http RequestConfig, 可通过{@link HttpUtils#setRequestConfig(RequestConfig)}方法，添加配置，
     * 在执行http请求前，会自动应用该配置，并在请求结束后，移除该配置，不影响后续的请求。
     * <p>
     * 注意：如果调用{@link HttpUtils#setRequestConfig(RequestConfig)}方法，添加了配置，但却没有执行http请求，
     * 请务必删除调用{@link HttpUtils#removeRequestConfig()}方法，移除配置，避免影响下一次http请求
     *
     * @param url         请求地址
     * @param requestBody 请求体
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         封装的io异常，将checked io exception转换为 unchecked exception
     */
    public static String doPost(String url, String requestBody) {
        return doPost(url, "application/json", null, null, requestBody);
    }

    /**
     * post请求，Content-Type：application/json
     * <p>
     * 如果需要修改http RequestConfig, 可通过{@link HttpUtils#setRequestConfig(RequestConfig)}方法，添加配置，
     * 在执行http请求前，会自动应用该配置，并在请求结束后，移除该配置，不影响后续的请求。
     * <p>
     * 注意：如果调用{@link HttpUtils#setRequestConfig(RequestConfig)}方法，添加了配置，但却没有执行http请求，
     * 请务必删除调用{@link HttpUtils#removeRequestConfig()}方法，移除配置，避免影响下一次http请求
     *
     * @param url         请求地址
     * @param headers     header信息
     * @param requestBody 请求体
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         封装的io异常，将checked io exception转换为 unchecked exception
     */
    public static String doPost(String url, Map<String, String> headers, String requestBody) {
        return doPost(url, "application/json", headers, null, requestBody);
    }

    /**
     * post请求。
     * <p>
     * 如果需要修改http RequestConfig, 可通过{@link HttpUtils#setRequestConfig(RequestConfig)}方法，添加配置，
     * 在执行http请求前，会自动应用该配置，并在请求结束后，移除该配置，不影响后续的请求。
     * <p>
     * 注意：如果调用{@link HttpUtils#setRequestConfig(RequestConfig)}方法，添加了配置，但却没有执行http请求，
     * 请务必删除调用{@link HttpUtils#removeRequestConfig()}方法，移除配置，避免影响下一次http请求
     *
     * @param url         请求地址
     * @param contentType Content-Type
     * @param headers     header信息
     * @param formParams  请求参数
     * @param requestBody 请求体
     * @return 响应体内容
     * @throws HttpExecutionFailException http请求执行失败，http status != 200
     * @throws WrappedIOException         封装的io异常，将checked io exception转换为 unchecked exception
     */
    public static String doPost(String url,
                                String contentType,
                                @Nullable Map<String, String> headers,
                                @Nullable Map<String, String> formParams,
                                @Nullable String requestBody) {
        LogContext logContext = LogContext.createInstance(true, Level.INFO).logger(log);
        ;
        try {
            logContext.append("http post请求，", url);
            logContext.append("Content-Type: ", contentType);
            Optional.ofNullable(headers).ifPresent(map -> logContext.append("headers: ", headers));
            Optional.ofNullable(formParams).ifPresent(map -> logContext.append("formParams: ", formParams));
            Optional.ofNullable(requestBody).ifPresent(str -> logContext.append("请求体: ", requestBody));

            // http httpPost
            HttpPost httpPost = new HttpPost(url);
            // 添加header
            if (headers != null && !headers.isEmpty()) {
                headers.forEach(httpPost::setHeader);
            }
            if (!httpPost.containsHeader("Content-Type")) {
                httpPost.addHeader("Content-Type", contentType);
            }
            // 设置form表单类型参数
            if (formParams != null && !formParams.isEmpty()) {
                List<BasicNameValuePair> pairs = new ArrayList<>();
                formParams.forEach((k, v) -> pairs.add(new BasicNameValuePair(k, v)));
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));
            }
            // 设置application/json类型的body参数
            if (StringUtils.hasText(requestBody)) {
                StringEntity entity = new StringEntity(requestBody, "UTF-8");
                httpPost.setEntity(entity);
            }
            // set RequestConfig
            Optional.ofNullable(getRequestConfig()).ifPresent(httpPost::setConfig);

            return executeHttpRequest(httpPost, logContext);
        } finally {
            logContext.print(false);
            removeRequestConfig();
            DISABLE_ERROR_LOG_MARK.remove();
        }
    }

    /**
     * 传输文件，支持<code>File</code>，<code>InputStream</code>，<code>byte[]</code>，<code>String</code>
     *
     * @param url        url
     * @param formParams 表单参数，key-参数名称，value-参数值（支持<code>File</code>，<code>InputStream</code>，
     *                   <code>byte[]</code>，<code>String</code>）。注意：暂时不支持设置fileName，会把key作为fileName
     * @param headers    header，可选参数
     * @return 响应体内容
     */
    public static String transferFiles(String url,
                                       Map<String, Object> formParams,
                                       @Nullable Map<String, String> headers) {
        LogContext logContext = LogContext.createInstance().logger(log);
        logContext.append("transferFiles, url: ", url);
        try {
            Optional.ofNullable(headers).ifPresent(map -> logContext.append("headers: ", headers));
            Optional.ofNullable(formParams).ifPresent(map -> logContext.append("formParams: ", formParams));

            HttpPost post = new HttpPost(url);
            // build MultipartEntity
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create()
                    .setCharset(StandardCharsets.UTF_8)
                    .setLaxMode();
            Optional.ofNullable(formParams).ifPresent(map -> map.forEach((k, v) -> {
                if (v instanceof String) {
                    entityBuilder.addTextBody(k, (String) v, ContentType.create("text/plain", Consts.UTF_8));
                }
                if (v instanceof File) {
                    File file = (File) v;
                    entityBuilder.addBinaryBody(k, file, ContentType.create("application/octet-stream", Consts.UTF_8), file.getName());
                }
                if (v instanceof byte[]) {
                    entityBuilder.addBinaryBody(k, (byte[]) v, ContentType.create("application/octet-stream", Consts.UTF_8), k);
                }
                if (v instanceof InputStream) {
                    entityBuilder.addBinaryBody(k, (InputStream) v, ContentType.create("application/octet-stream", Consts.UTF_8), k);
                }
            }));
            // add headers
            Optional.ofNullable(headers).ifPresent(map -> map.forEach(post::addHeader));
            // set entity
            post.setEntity(entityBuilder.build());

            return executeHttpRequest(post, logContext);
        } finally {
            logContext.print(false);
            removeRequestConfig();
            DISABLE_ERROR_LOG_MARK.remove();
        }
    }

    private static String executeHttpRequest(HttpUriRequest request, LogContext logContext) {
        CloseableHttpResponse response = null;
        try {
            long start = System.currentTimeMillis();
            response = httpClient.execute(request);
            long end = System.currentTimeMillis();
            logContext.append("http请求耗时：", (end - start), "ms");

            StatusLine statusLine = response.getStatusLine();
            String entity = EntityUtils.toString(response.getEntity());
            if (HttpStatus.SC_OK == statusLine.getStatusCode()) {
                logContext.append("请求成功，响应内容：", entity);
                return entity;
            }
            logContext.append(Level.WARN, "请求失败，http status code: ", statusLine.getStatusCode(), "，响应内容：", entity);
            throw new HttpExecutionFailException(String.format("http执行失败，http status code：%s, 响应内容：%s",
                    statusLine.getStatusCode(), entity));
        } catch (IOException e) {
            if (DISABLE_ERROR_LOG_MARK.get()) {
                log.warn("http request exception", e);
                logContext.append(Level.WARN, "http请求异常：", e.getMessage());
            } else {
                log.error("http request exception", e);
                logContext.append(Level.ERROR, "http请求异常：", e.getMessage());
            }
            throw new WrappedIOException("http请求异常, " + e.getMessage(), e);
        } finally {
            Optional.ofNullable(response).ifPresent(HttpUtils::releaseConn);
        }
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
        return RequestConfig.custom()
                .setConnectTimeout(connectTimeoutMills) // 连接超时时间
                .setSocketTimeout(socketTimeoutMills) // 请求获取数据的超时时间
                .setConnectionRequestTimeout(connectionRequestTimeoutMills) // 从连接池中获取连接的超时时间
                .build();
    }

    /**
     * 释放http connection
     *
     * @param response CloseableHttpResponse
     */
    private static void releaseConn(CloseableHttpResponse response) {
        try {
            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            log.error("关闭io流异常", e);
        }
    }

    /**
     * 设置RequestConfig，与当前线程绑定
     *
     * @param config http配置信息
     * @since 1.1.0
     */
    public static void setRequestConfig(RequestConfig config) {
        REQUEST_CONFIG_CONTEXT.set(config);
    }

    /**
     * 获取与当前线程绑定的RequestConfig，如果不存在，return null
     *
     * @since 1.1.0
     */
    @Nullable
    public static RequestConfig getRequestConfig() {
        return REQUEST_CONFIG_CONTEXT.get();
    }

    /**
     * 移除当前线程绑定的RequestConfig
     *
     * @since 1.1.0
     */
    public static void removeRequestConfig() {
        REQUEST_CONFIG_CONTEXT.remove();
    }

    /**
     * 禁用error日志，当http请求执行失败时，使用warn日志代替error日志。注意：只影响下一次http请求
     */
    public static void disableHttpRequestFailErrorLog() {
        DISABLE_ERROR_LOG_MARK.set(true);
    }
}
