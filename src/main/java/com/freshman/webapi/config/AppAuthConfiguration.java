package com.freshman.webapi.config;

import com.freshman.webapi.filters.AuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * 配置拦截器
 */
@Configuration
public class AppAuthConfiguration extends WebMvcConfigurerAdapter {
    /**
     * 关键，将拦截器作为bean写入配置中
     */
    @Bean
    public AuthInterceptor getSecurityInterceptor() {
        return new AuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        InterceptorRegistration ir = registry.addInterceptor(getSecurityInterceptor());
        // 配置拦截的路径
        ir.addPathPatterns("/*");
        //配置不拦截的路径
        ir.excludePathPatterns("**/swagger-ui.html");
        //还可以在这里注册其它的拦截器
        //registry.addInterceptor(new AppAuthInterceptor()).addPathPatterns("/*");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUserMethodArgumentResolver());
        super.addArgumentResolvers(argumentResolvers);
    }

    /**
     * 参数解析器
     * @return
     */
    @Bean
    public CurrentUserMethodArgumentResolver currentUserMethodArgumentResolver() {
        return new CurrentUserMethodArgumentResolver();
    }
}
