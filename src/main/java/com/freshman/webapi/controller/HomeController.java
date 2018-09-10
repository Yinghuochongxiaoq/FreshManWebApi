package com.freshman.webapi.controller;

import com.alibaba.fastjson.JSONObject;
import com.freshman.webapi.config.UploadConfig;
import com.freshman.webapi.filters.CurrentUser;
import com.freshman.webapi.filters.IgnoreSecurity;
import com.freshman.webapi.model.BaseResponse;
import com.freshman.webapi.model.ResponseCode;
import com.freshman.webapi.pojo.User;
import com.freshman.webapi.security.SunPasswordEncoder;
import com.freshman.webapi.service.RedisService;
import com.freshman.webapi.service.UserService;
import com.freshman.webapi.util.SessionUtil;
import com.freshman.webapi.util.fileutil.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;

@Api(value = "首页控制器", tags = {"HomeController"})
@RestController
public class HomeController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UploadConfig uploadConfig;

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

    /**
     * 文件上传
     *
     * @param file 文件列表
     * @return
     */
    @ApiOperation(value = "文件上传", notes = "文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "文件列表", required = true, dataType = "File", paramType = "query")
    })
    @IgnoreSecurity
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public BaseResponse upload(@RequestParam("file") MultipartFile[] file) {
        try {
            FileUtil.saveUploadFiles(Arrays.asList(file), uploadConfig.getUploadpath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BaseResponse();
    }

    /**
     * 下载示例文件
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "下载示例文件", notes = "下载示例文件")
    @IgnoreSecurity
    @RequestMapping(value = "downLoadFile", method = RequestMethod.GET)
    public BaseResponse downLoadFile(HttpServletRequest request, HttpServletResponse response) {
        BaseResponse baseResponse = new BaseResponse();
        String filename = "支付码.jpg";
        String filePath = uploadConfig.getUploadpath() + filename;

        try {
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                filename = URLEncoder.encode(filename, "UTF-8");
            } else {
                filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        File file = new File(filePath);
        if (!file.exists()) {
            baseResponse.setCode(ResponseCode.FAIL);
            baseResponse.setMessage("文件不存在");
            return baseResponse;
        }
        response.setContentType("application/force-download");
        response.setHeader("Content-Disposition", "attachment;fileName=" + filename);
        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        BufferedInputStream bufferedInputStream = null;
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            fis = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fis);
            int i = bufferedInputStream.read(buffer);
            while (i != -1) {
                os.write(buffer);
                i = bufferedInputStream.read(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bufferedInputStream.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        baseResponse.setCode(ResponseCode.SUCCESS);
        return baseResponse;
    }
}
