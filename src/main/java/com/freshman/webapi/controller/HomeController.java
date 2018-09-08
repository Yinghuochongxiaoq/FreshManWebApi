package com.freshman.webapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.freshman.webapi.filters.CurrentUser;
import com.freshman.webapi.filters.IgnoreSecurity;
import com.freshman.webapi.model.BaseResponse;
import com.freshman.webapi.model.ResponseCode;
import com.freshman.webapi.pojo.User;
import com.freshman.webapi.security.SunPasswordEncoder;
import com.freshman.webapi.service.RedisService;
import com.freshman.webapi.service.UserService;
import com.freshman.webapi.util.SessionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Api(value = "首页控制器", tags = {"HomeController"})
@RestController
public class HomeController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    /***
     * 测试地址
     * @return
     */
    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public String home() {
        return "Hello World!";
    }

    /**
     * 验证登录
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "验证登录", notes = "验证登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query"),
    })
    @IgnoreSecurity
    @RequestMapping(value = "checkUserPwd", method = {RequestMethod.POST, RequestMethod.GET})
    public BaseResponse checkUserPwd(HttpServletRequest request, String username, String password) {
        BaseResponse baseResponse = new BaseResponse();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            baseResponse.setCode(ResponseCode.FAIL);
            baseResponse.setMessage("参数不能为空");
            return baseResponse;
        }
        JSONObject jsonObject = new JSONObject();

        SunPasswordEncoder md5 = new SunPasswordEncoder();
        password = md5.encodePassword(password, username);
        User user = userService.getUserId(1);
        jsonObject.put("dbUser", user);
        if (redisService.setValue(username, user)) {
            jsonObject.put("setRedis", "缓存存入成功");
            Object redisUser = redisService.getValue(username);
            redisService.deleteKey(username);
            jsonObject.put("getRedis", redisUser);
        }
        User admin = userService.getUser(username, password);
        if (admin == null) {
            baseResponse.setCode(ResponseCode.FAIL);
            baseResponse.setMessage("用户名或密码错误");
            baseResponse.setData(jsonObject);
            return baseResponse;
        }
        jsonObject.put("checkUser", true);
        HttpSession session = SessionUtil.getSessionByRequest(request, true);
        session.setAttribute("user", admin);
        baseResponse.setCode(ResponseCode.SUCCESS);
        baseResponse.setMessage("登录成功");
        baseResponse.setData(jsonObject);
        return baseResponse;
    }

    /**
     * 登出
     *
     * @param userInfo
     * @return
     */
    @ApiOperation(value = "用户登出", notes = "用户登出")
    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public BaseResponse logout(@CurrentUser String userInfo) {
        return new BaseResponse(ResponseCode.SUCCESS, userInfo);
    }
}
