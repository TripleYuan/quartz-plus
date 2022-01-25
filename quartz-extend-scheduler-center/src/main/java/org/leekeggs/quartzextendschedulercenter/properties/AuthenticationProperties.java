package org.leekeggs.quartzextendschedulercenter.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录用户配置
 *
 * @author redcoder54
 * @since 2022-01-11
 */
@Configuration
@ConfigurationProperties(prefix = "authentication")
@Getter
@Setter
public class AuthenticationProperties {

    /**
     * 鉴权cookie配置
     */
    private CookieConfig cookieConfig;

    /**
     * 允许登录的用户
     */
    private List<User> users = new ArrayList<>();

    @Getter
    @Setter
    public static class CookieConfig {
        /**
         * cookie's name, default value: user-id
         */
        private String name = "user-id";
        /**
         * cookie domain, default value: localhost
         */
        private String domain = "localhost";
        /**
         * cookie path, default value: /
         */
        private String path = "/";
        /**
         * the maximum age of the cookie in seconds, default value: 86400 (24h)
         */
        private int maxAge = 24 * 60 * 60;
    }

    @Getter
    @Setter
    public static class User {
        private String name;
        private String password;
    }
}
