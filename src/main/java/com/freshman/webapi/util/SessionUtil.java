package com.freshman.webapi.util;

import com.freshman.webapi.pojo.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.UUID;

public class SessionUtil {
    /**
     * 获取session
     * 第二个参数(enforce)是否强制生成session(true|false)
     *
     * @param request
     * @param enforce
     * @return HttpSession
     */
    public static HttpSession getSessionByRequest(HttpServletRequest request, boolean enforce) {
        return request.getSession(enforce);
    }

    /**
     * 获取验证码
     *
     * @param request
     * @return
     */
    public static String getVerifycode(HttpServletRequest request) {
        HttpSession session = getSessionByRequest(request, false);
        String rand = (String) session.getAttribute("verifycode");
        return rand;
    }
    /**
     * 获取短信验证码
     *
     * @param request
     * @return
     */
    public static String getSmsVerifycode(HttpServletRequest request) {
        HttpSession session = getSessionByRequest(request, false);
        String rand = (String) session.getAttribute("sms_verifycode");
        return rand;
    }
    public static String getSmsVerifyCodeBySession(HttpServletRequest request) {
		HttpSession session = SessionUtil.getSessionByRequest(request, false);
		if (session == null) {
			return "";
		}
		if (session.getAttribute("sms_verifycode") == null) {
			return "";
		}
		return session.getAttribute("sms_verifycode").toString();
	}
//    
//    /**
//     * 获取用户集团id
//     *
//     * @param request
//     * @return
//     */
//    public static Integer getGroupID(HttpServletRequest request) {
//        Admin temp = getUser(request);
//        if (temp == null)
//            return null;
//        return temp.getGroupid();
//    }
    /**
     * 获取用户
     *
     * @param request
     * @return
     */
    public static User getUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }
    /**
     * 获取UUID
     */
    public static String getUUID() {
    	return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
