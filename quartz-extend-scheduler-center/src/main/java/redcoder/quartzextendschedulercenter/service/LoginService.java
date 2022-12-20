package redcoder.quartzextendschedulercenter.service;

import redcoder.quartzextendschedulercenter.dto.login.LoginDTO;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录服务
 *
 * @author redcoder54
 * @since 2022-01-11
 */
public interface LoginService {

    /**
     * 用户登录
     *
     * @param loginDTO 登录用户信息
     * @return 是否登录成功
     */
    boolean login(LoginDTO loginDTO);

    /**
     * 判断用户是否登录
     *
     * @return 如果用户已登录，返回true
     */
    boolean isLogin(HttpServletRequest httpServletRequest);
}
