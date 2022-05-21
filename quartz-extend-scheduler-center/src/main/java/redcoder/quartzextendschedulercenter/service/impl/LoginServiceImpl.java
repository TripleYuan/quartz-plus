package redcoder.quartzextendschedulercenter.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import redcoder.quartzextendschedulercenter.model.dto.login.LoginDTO;
import redcoder.quartzextendschedulercenter.properties.AuthenticationProperties;
import redcoder.quartzextendschedulercenter.service.LoginService;
import redcoder.quartzextendschedulercenter.utils.HttpServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author redcoder54
 * @since 2022-01-11
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationProperties properties;
    @Autowired
    private Environment environment;

    // 已登录用户, name: user-id, value: username
    private final Cache<String, String> loginUserCache = CacheBuilder.newBuilder()
            .maximumSize(Integer.MAX_VALUE)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build();

    @Override
    public boolean login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        if (isValidUser(username, password)) {
            // 登录成功
            processOnLoginSuccess(username, password);
            return true;
        } else {
            // 登录失败
            processOnLoginFail(username, password);
            return false;
        }
    }

    @Override
    public boolean isLogin(HttpServletRequest httpServletRequest) {
        // if (!"PROD".equals(environment.getActiveProfiles()[0])) {
        //     // 仅生产环境需要登录
        //     return true;
        // }
        String userid = getUserid(httpServletRequest);
        return userid != null && loginUserCache.getIfPresent(userid) != null;
    }

    private boolean isValidUser(String username, String password) {
        for (AuthenticationProperties.User user : properties.getUsers()) {
            if (username.equals(user.getName()) && password.equals(user.getPassword())) {
                return true;
            }
        }
        return false;
    }

    private void processOnLoginSuccess(String username, String password) {
        log.info("用户登录成功. [username={}]", username);
        String userid = UUID.randomUUID().toString();
        Cookie cookie = new Cookie(properties.getCookieConfig().getName(), userid);
        cookie.setMaxAge(properties.getCookieConfig().getMaxAge());
        cookie.setPath(properties.getCookieConfig().getPath());
        cookie.setDomain(properties.getCookieConfig().getDomain());
        HttpServletUtils.getResponse().addCookie(cookie);

        loginUserCache.put(userid, username);
    }

    private void processOnLoginFail(String username, String password) {
        log.warn("用户登录失败. [username={}, password={}]", username, password);
    }

    @Nullable
    private String getUserid(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (Objects.equals(cookie.getName(), properties.getCookieConfig().getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
