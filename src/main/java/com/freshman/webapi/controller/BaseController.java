package com.freshman.webapi.controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 控制器基类
 */
public class BaseController {

    @Resource
    private HttpServletRequest request;

    /**
     * 初始化方法
     * @param request
     * @param response
     * @throws Exception
     */
    @ModelAttribute
    public final void init(HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.request = request;
    }
}
