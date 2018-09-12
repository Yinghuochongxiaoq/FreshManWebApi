package com.freshman.webapi.filters;

import com.alibaba.fastjson.JSON;
import com.freshman.webapi.model.BaseResponse;
import com.freshman.webapi.model.ResponseCode;
import com.freshman.webapi.pojo.User;
import com.freshman.webapi.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;

/**
 * 用户验证拦截器，可以自定义
 */
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String requestPath = request.getRequestURI();
        logger.debug("Method: " + method.getName() + ", IgnoreSecurity: " + method.isAnnotationPresent(IgnoreSecurity.class));
        logger.debug("requestPath:" + requestPath);
        if (requestPath.contains("/v2/api-docs") || requestPath.contains("/swagger") || requestPath.contains("/configuration/ui")) {
            return true;
        }
        if (requestPath.contains("/error")) {
            return true;
        }
        if (method.isAnnotationPresent(IgnoreSecurity.class)) {
            return true;
        }
        //TODO：验证用户登录，或者你需要的信息，比如这里验证logout需要请求头信息
        if(requestPath.contains("/logout")){
            String token = request.getHeader("ACCESS_TOKEN");
            logger.debug("token: " + token);
            if (StringUtils.isEmpty(token)) {
                filterError(request, response, new BaseResponse(ResponseCode.HTTPMESSAGENOTWRITABLEEXCEPTION, "request header key ACCESS_TOKEN is empty"));
                return false;
            }
        }
        //TODO:可以结合数据库处理数据
        User user = userService.getUserId(1);
        //TODO: 这儿的currentUse和CurrentUserMethodArgumentResolver.resolveArgument 方法中的currentUser需要一致
        request.setAttribute("currentUser", user.getName());
        return true;
    }

    /**
     * 设置错误信息
     *
     * @param req
     * @param res
     * @param response
     * @return
     * @throws IOException
     * @throws ServletException
     */
    private BaseResponse filterError(ServletRequest req, ServletResponse res, BaseResponse response) throws IOException, ServletException {
        PrintWriter writer = null;
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(res.getOutputStream(), "UTF-8");
            writer = new PrintWriter(osw, true);
            String jsonStr = JSON.toJSONString(response);
            writer.write(jsonStr);
            writer.flush();
            writer.close();
            osw.close();
        } catch (Exception e) {
            logger.error("过滤器返回信息失败:" + e.getMessage(), e);
        } finally {
            if (null != writer) {
                writer.close();
            }
            if (null != osw) {
                osw.close();
            }
        }
        return response;
    }
}
