package redcoder.quartzextendschedulercenter.web.filter;

import redcoder.quartzextendcommon.utils.JsonUtils;
import redcoder.quartzextendschedulercenter.dto.ApiResult;
import redcoder.quartzextendschedulercenter.service.LoginService;
import redcoder.quartzextendschedulercenter.utils.WebUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static redcoder.quartzextendschedulercenter.constant.ApiStatus.UNAUTHORIZED_REQUEST;

/**
 * 授权拦截器，验证用户是否登录
 *
 * @author redcoder
 * @since 2022-01-11
 */
public class AuthenticateFilter implements Filter {

    private LoginService loginService;

    public AuthenticateFilter(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (!loginService.isLogin(httpServletRequest)) {
            // 未登录
            ApiResult<String> error = ApiResult.failure(UNAUTHORIZED_REQUEST);
            WebUtils.writeResponse(httpServletResponse, JsonUtils.beanToJsonString(error));
            return;
        }
        chain.doFilter(request, response);
    }

}
