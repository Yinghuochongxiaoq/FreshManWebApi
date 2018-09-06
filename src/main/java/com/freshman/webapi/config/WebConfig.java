package com.freshman.webapi.config;

import com.freshman.webapi.filters.SystemUrlFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;

@Configuration
public class WebConfig {
    /**
     * 拦截用户访问系统权限
     * @return
     */
    @Bean
    public FilterRegistrationBean systemUrlFilterBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new DelegatingFilterProxy("systemUrlFilter"));
        registrationBean.addInitParameter("targetFilterLifecycle","true");
        registrationBean.addUrlPatterns("/*");
        registrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/reg");
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        return registrationBean;
    }

    @Bean(name="systemUrlFilter")
    public SystemUrlFilter systemUrlFilter(){
        return new SystemUrlFilter();
    }
}
