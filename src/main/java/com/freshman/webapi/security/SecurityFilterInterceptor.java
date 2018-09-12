package com.freshman.webapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import javax.servlet.*;
import java.io.IOException;


@Service
public class SecurityFilterInterceptor extends AbstractSecurityInterceptor implements Filter {

    @Autowired
    private FilterInvocationSecurityMetadataSource securityMetadataSource;

    @Autowired
    public void setMyAccessDecisionManager(SecurityAccessDecisionManager myAccessDecisionManager) {
        super.setAccessDecisionManager(myAccessDecisionManager);
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(request, response, chain);
        invoke(fi);
    }

    private void invoke(FilterInvocation fi) throws IOException, ServletException {
        // object为FilterInvocation对象
        //super.beforeInvocation(fi);源码
        //1.获取请求资源的权限
        //执行Collection<ConfigAttribute> attributes = SecurityMetadataSource.getAttributes(object);
        //2.是否拥有权限
        //this.accessDecisionManager.decide(authenticated, object, attributes);
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
        return securityMetadataSource;
    }

    public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource securityMetadataSource) {
        this.securityMetadataSource = securityMetadataSource;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {

    }

    @Override
    public Class<? extends Object> getSecureObjectClass() {
        //下面的MyAccessDecisionManager的supports方面必须放回true,否则会提醒类型错误
        return FilterInvocation.class;
    }
}
