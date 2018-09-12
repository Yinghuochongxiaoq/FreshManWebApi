package com.freshman.webapi.config;

import com.freshman.webapi.security.SecurityFilterInterceptor;
import com.freshman.webapi.security.SunPasswordEncoder;
import com.freshman.webapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SecurityFilterInterceptor myFilterSecurityInterceptor;

    @Autowired
    UserDetailsService customUserService;

    /**
     * 注册UserDetailsService 的bean
     *
     * @return
     */
    @Bean
    UserDetailsService customUserService() {
        return new UserService();
    }

    /**
     * user Details Service验证
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService).passwordEncoder(new SunPasswordEncoder());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.GET,
//                        "/",
//                        "/login**",
//                        "/logout**",
//                        "/*.html",
//                        "/favicon.ico",
//                        "/**/*.html",
//                        "/**/*.css",
//                        "/**/*.js",
//                        "/webjars/**",
//                        "/swagger-resources/**",
//                        "/*/api-docs").permitAll()
//                .anyRequest().authenticated().and()
//                .formLogin()
//                //.loginPage("/login")//定义自己的login页面
//                .defaultSuccessUrl("/")
//                .failureUrl("/login?error")
//                .permitAll() //登录页面用户任意访问
//                .and()
//                .logout()
//                .permitAll(); //注销行为任意访问;
        http.authorizeRequests().anyRequest().permitAll();
        http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);
    }
}
