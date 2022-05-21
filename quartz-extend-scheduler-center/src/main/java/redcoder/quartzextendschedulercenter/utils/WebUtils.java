package redcoder.quartzextendschedulercenter.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * web工具类
 *
 * @author redcoder54
 * @since 2021-03-10
 */
public class WebUtils {

    /**
     * 向客户端返回一些数据
     *
     * @param response     HttpServletResponse
     * @param responseBody 要输出到客户端的数据
     * @throws IOException if an output exception occurred
     */
    public static void writeResponse(HttpServletResponse response, String responseBody) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.addHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        response.getWriter().println(responseBody);
    }

    /**
     * 向客户端返回一些数据
     *
     * @param response     HttpServletResponse
     * @param responseBody 要输出到客户端的数据
     * @param headers      要添加到response的header
     * @throws IOException if an output exception occurred
     */
    public static void writeResponse(HttpServletResponse response, String responseBody, Map<String, String> headers)
            throws IOException {
        Assert.notNull(headers, "'headers' must not be null");
        response.addHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
        // add header
        headers.forEach(response::addHeader);

        response.getWriter().println(responseBody);
    }

    /**
     * 获取与跨域相关的header：
     * <ul>
     *     <li>Access-Control-Allow-Headers: *</li>
     *     <li>Access-Control-Allow-Methods: *</li>
     *     <li>Access-Control-Allow-Origin: *</li>
     *     <li>Access-Control-Expose-Headers: *</li>
     *     <li>Access-Control-Max-Age: 3600</li>
     * </ul>
     */
    public static Map<String, String> getCorsHeaders() {
        Map<String, String> corsHeaders = new HashMap<>();
        /*corsHeaders.put(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");*/
        corsHeaders.put(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        corsHeaders.put(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");
        corsHeaders.put(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        corsHeaders.put(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*");
        corsHeaders.put(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
        return corsHeaders;
    }
}
