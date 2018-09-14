package com.freshman.webapi.controller;

import com.freshman.webapi.model.BaseResponse;
import com.freshman.webapi.model.ResponseCode;
import com.freshman.webapi.pojo.User;
import com.freshman.webapi.security.SunPasswordEncoder;
import com.freshman.webapi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Shiro测试Controller
 */
@Api(value = "Shiro测试控制器", tags = {"ShiroController"})
@RestController
@RequestMapping(value = "/shiro")
public class ShiroController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 登陆页面
     *
     * @return
     */
    @ApiOperation(value = "登录页面", notes = "登录页面")
    @RequestMapping(value = "sLogin", method = RequestMethod.GET)
    public String loginPage() {
        return "This is login page.";
    }

    /**
     * 登陆页面
     *
     * @return
     */
    @ApiOperation(value = "验证登录", notes = "验证登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "sLoginAjax", method = RequestMethod.GET)
    public BaseResponse loginPage(String username, String password) {
        BaseResponse baseResponse = new BaseResponse();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            baseResponse.setCode(ResponseCode.FAIL);
            baseResponse.setMessage("参数不能为空");
            return baseResponse;
        }

        SunPasswordEncoder md5 = new SunPasswordEncoder();
        password = md5.encodePassword(password, username);

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        //获取当前的Subject
        Subject currentUser = SecurityUtils.getSubject();
        try {
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
            System.out.println("对用户[" + username + "]进行登录验证..验证开始");
            currentUser.login(token);
            System.out.println("对用户[" + username + "]进行登录验证..验证通过");
        } catch (UnknownAccountException uae) {
            System.out.println("对用户[" + username + "]进行登录验证..验证未通过,未知账户");
            baseResponse.setCode(ResponseCode.FAIL);
            baseResponse.setMessage("未知账户");
        } catch (IncorrectCredentialsException ice) {
            System.out.println("对用户[" + username + "]进行登录验证..验证未通过,错误的凭证");
            baseResponse.setCode(ResponseCode.FAIL);
            baseResponse.setMessage("密码不正确");
        } catch (LockedAccountException lae) {
            System.out.println("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");
            baseResponse.setCode(ResponseCode.FAIL);
            baseResponse.setMessage("账户已锁定");
        } catch (ExcessiveAttemptsException eae) {
            System.out.println("对用户[" + username + "]进行登录验证..验证未通过,错误次数过多");
            baseResponse.setCode(ResponseCode.FAIL);
            baseResponse.setMessage("错误次数过多");
        } catch (AuthenticationException ae) {
            //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
            System.out.println("对用户[" + username + "]进行登录验证..验证未通过,堆栈轨迹如下");
            ae.printStackTrace();
            baseResponse.setCode(ResponseCode.FAIL);
            baseResponse.setMessage("用户名或密码不正确");
        }
        //验证时候登录成功
        if (currentUser.isAuthenticated()) {
            System.out.println("用户[" + username + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
            baseResponse.setCode(ResponseCode.SUCCESS);
            baseResponse.setMessage("redirect:/user");
            return baseResponse;
        } else {
            token.clear();
            baseResponse.setCode(ResponseCode.FAIL);
            baseResponse.setMessage("验证登录失败");
            return baseResponse;
        }
    }

    /**
     * 使用权限管理工具进行用户的退出，跳出登录，给出提示信息
     *
     * @param redirectAttributes
     * @return
     */
    @ApiOperation(value = "跳出登录", notes = "跳出登录")
    @RequestMapping(value = "/sLogout", method = RequestMethod.GET)
    public String logout(RedirectAttributes redirectAttributes) {
        SecurityUtils.getSubject().logout();
        redirectAttributes.addFlashAttribute("message", "您已安全退出");
        return "redirect:/login";
    }

    /**
     * 没有权限提示
     *
     * @return
     */
    @ApiOperation(value = "无权限提示", notes = "无权限提示")
    @RequestMapping("/403")
    public String unauthorizedRole() {
        return "403 没有权限";
    }

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "int", paramType = "query")
    })
    @RequestMapping("/user")
    public User getUserList(int id) {
        User user = userService.getUserId(id);
        return user;
    }

    /**
     * 编辑用户信息
     *
     * @param userid
     * @return
     */
    @ApiOperation(value = "编辑用户信息", notes = "编辑用户信息")
    @RequestMapping("/user/edit/{userid}")
    public String editUser(@PathVariable int userid) {
        return "user_edit 进入用户信息修改";
    }

    /**
     * 删除用户信息，demo中没有添加删除权限，目的是为了实现无权限的效果
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "删除用户信息", notes = "删除用户信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "int", paramType = "query")})
    @RequestMapping("/user/delete")
    public String deleteUser(int userId) {
        return "deleteUser " + userId + "进入删除用户";
    }

    /**
     * 测试权限注解的方式
     *
     * @return
     */
    @ApiOperation(value = "删除用户信息", notes = "删除用户信息")
    @RequiresPermissions(value = {"user:edit"})
    @RequiresRoles(value = {"admin", "manager"}, logical = Logical.OR)
    @RequestMapping(value = "/rolePermission")
    public BaseResponse rolePermission() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(ResponseCode.SUCCESS);
        baseResponse.setMessage("注解使用权限配置");
        return baseResponse;
    }
}
