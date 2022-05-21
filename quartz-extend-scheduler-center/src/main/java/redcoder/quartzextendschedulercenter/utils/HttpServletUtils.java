package redcoder.quartzextendschedulercenter.utils;

import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 提供获取{@link HttpServletRequest}和{@link HttpServletResponse}对象的工具类
 *
 * @author redcoder54
 * @since 2022-01-11
 */
public class HttpServletUtils {

    /**
     * 获取{@link HttpServletRequest}对象
     *
     * @throws IllegalStateException 未获取到{@link HttpServletRequest}对象时，抛出异常
     * @see RequestContextHolder
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Assert.state(request != null, "未获取到HttpServletRequest对象");
        return request;
    }

    /**
     * 获取{@link HttpServletResponse}对象
     *
     * @throws IllegalStateException 未获取到{@link HttpServletResponse}对象时，抛出异常
     * @see RequestContextHolder
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        Assert.state(response != null, "未获取到HttpServletResponse对象");
        return response;
    }
}
