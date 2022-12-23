package redcoder.quartzextendschedulercenter.shiro;

import com.google.common.collect.Lists;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.AbstractShiroWebConfiguration;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redcoder.quartzextendcommon.utils.MapUtils;

import java.util.concurrent.TimeUnit;

/**
 * shiro配置
 *
 * @author redcoder
 * @since 2022-12-20
 */
@Configuration
public class ShiroConfig extends AbstractShiroWebConfiguration {

    @Value("${shiro.session.valid-days}")
    private int expireInDays;

    @Autowired
    private MemorySessionDAO sessionDAO;
    @Autowired
    private QuartzRealm realm;

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/api/login", "anon");
        chainDefinition.addPathDefinition("/api/quartz-job-scheduler/**", "anon");
        chainDefinition.addPathDefinition("/api/**", "authc");
        return chainDefinition;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager());
        filterFactoryBean.setGlobalFilters(Lists.newArrayList(DefaultFilter.invalidRequest.name()));
        filterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition().getFilterChainMap());
        filterFactoryBean.setFilters(MapUtils.buildMap("authc", new QuartzFormAuthenticationFilter()));

        return filterFactoryBean;
    }

    @Bean
    public SessionsSecurityManager securityManager() {
        return super.securityManager(Lists.newArrayList(realm));
    }

    @Override
    protected SessionManager sessionManager() {
        DefaultWebSessionManager webSessionManager = new DefaultWebSessionManager();
        webSessionManager.setSessionIdCookieEnabled(sessionIdCookieEnabled);
        webSessionManager.setSessionIdUrlRewritingEnabled(sessionIdUrlRewritingEnabled);
        webSessionManager.setSessionIdCookie(sessionCookieTemplate());

        webSessionManager.setSessionFactory(sessionFactory());
        webSessionManager.setSessionDAO(sessionDAO());
        webSessionManager.setDeleteInvalidSessions(sessionManagerDeleteInvalidSessions);

        webSessionManager.setGlobalSessionTimeout(TimeUnit.DAYS.toMillis(expireInDays));
        webSessionManager.setSessionValidationInterval(TimeUnit.DAYS.toMillis(1));

        return webSessionManager;
    }

    /**
     * 返回{@link MemorySessionDAO}对象
     */
    @Override
    protected SessionDAO sessionDAO() {
        return sessionDAO;
    }
}
