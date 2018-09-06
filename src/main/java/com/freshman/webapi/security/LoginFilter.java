package com.freshman.webapi.security;

import com.freshman.webapi.pojo.User;
import com.freshman.webapi.util.SessionUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class LoginFilter implements Filter {

    private Set<String> prefixIignores = new HashSet<String>();

    public void init(FilterConfig filterConfig) throws ServletException {
        String cp = filterConfig.getServletContext().getContextPath();
        String ignoresParam = filterConfig.getInitParameter("ignores");
        String[] ignoreArray = ignoresParam.split(",");
        for (String s : ignoreArray) {
            prefixIignores.add(cp + s);
        }
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        //获取请求路径
        String path = request.getRequestURI();
        String contextPath = request.getContextPath();

        if (canIgnore(request)) {
            filterChain.doFilter(request, response);
            return;
        }


        if (contextPath != "/") {
            contextPath = contextPath + "/";
        }
        //获取session中作为判断的字段
        User user = SessionUtil.getUser(request);
        if (user == null || user.getId() < 1) {
            if (path.indexOf("/login") < 0 && !path.equalsIgnoreCase(contextPath)) {
                response.sendRedirect(contextPath + "/login");
            }
        }
        filterChain.doFilter(request, response);
    }

    public void destroy() {
        prefixIignores = null;
    }

    private boolean canIgnore(HttpServletRequest request) {
        String url = request.getRequestURI();
        for (String ignore : prefixIignores) {
            if (url.startsWith(ignore)) {
                return true;
            }
        }
        return false;
    }

}
