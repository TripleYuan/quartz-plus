package redcoder.quartzextendschedulercenter.service;

import redcoder.quartzextendschedulercenter.dto.login.LoginDTO;

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
     * 登出
     */
    void logout();

}
