package redcoder.quartzextendschedulercenter.web;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redcoder.quartzextendschedulercenter.web.filter.SimpleCorsFilter;

/**
 * @author redcoder
 * @since 2021-03-10
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<SimpleCorsFilter> corsFilterRegisterBean() {
        FilterRegistrationBean<SimpleCorsFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new SimpleCorsFilter());
        filterRegistrationBean.addUrlPatterns("/api/*");
        filterRegistrationBean.setOrder(0);
        return filterRegistrationBean;
    }
}
