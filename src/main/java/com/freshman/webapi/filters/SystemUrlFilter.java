package com.freshman.webapi.filters;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Url过滤器
 */
public class SystemUrlFilter implements Filter {

    private String[] prefixIignores;
    private String ignoresParam;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ignoresParam = filterConfig.getInitParameter("exclusions");
        if (!StringUtils.isBlank(ignoresParam)) {
            prefixIignores = ignoresParam.split(",");
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //TODO:在过滤器之外
        if (canIgnore(request)) {
            filterChain.doFilter(request, servletResponse);
            return;
        }
        //TODO:在过滤器之中
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {
        prefixIignores = null;
    }

    private boolean canIgnore(HttpServletRequest request) {
        boolean isExcludedPage = false;
        for (String page : prefixIignores) {// 判断是否在过滤url之外
            if (request.getServletPath().equals(page)) {
                isExcludedPage = true;
                break;
            }
        }
        return isExcludedPage;
    }
}
