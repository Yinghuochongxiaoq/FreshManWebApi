package com.freshman.webapi.security;

import com.freshman.webapi.util.md5.MD5Util;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;

public class SunPasswordEncoder implements PasswordEncoder {

	//@实现加密的方法，既将明文转换为密文的方法
	 public String encodePassword(String rawPass, Object salt)
	   throws DataAccessException {
	  String pass = null;
	  try {
	   //pass = Tools.encryptBasedDes(rawPass);
		 pass=MD5Util.md5(rawPass).toLowerCase();
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	  return pass;
	 }

	 //@验证密码是否有效的方法，返回'true'则登录成功

	 public boolean isPasswordValid(String encPass, String rawPass, Object salt)
	   throws DataAccessException {
	  return encPass.equals(MD5Util.md5(rawPass).toLowerCase());
	 }
}