package com.freshman.webapi.shiro;

import com.freshman.webapi.pojo.User;
import com.freshman.webapi.service.UserService;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;

    /**
     * 权限认证，为当前登录的Subject授予角色和权限
     * 经测试：本例中该方法的调用时机为需授权资源被访问时
     * 并且每次访问需授权资源时都会执行该方法中的逻辑，这表明本例中默认并未启用AuthorizationCache
     * 经测试：如果连续访问同一个URL（比如刷新），该方法不会被重复调用，Shiro有一个时间间隔（也就是cache时间，在ehcache-shiro.xml中配置），超过这个时间间隔再刷新页面，该方法会被执行
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("##################执行Shiro权限认证##################");
        //获取当前登录输入的用户名
        String loginName = (String) super.getAvailablePrincipal(principalCollection);

        //region TODO:获取登录用户信息
        User user = userService.getUserByName(loginName);
        //如果没有查询到返回null就会导致任何用户访问被拦截的请求时，都会自动跳转到unauthorizedUrl指定的地方
        if (null == user) {
            return null;
        }
        //endregion

        //权限信息对象info，用来存放查出来的用户的所有角色（role)及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //region TODO:用户的角色集合，需要实现自己的角色集合,获取当前用户的role信息
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        roles.add("manager");
        roles.add("normal");
        //endregion

        info.setRoles(roles);

        //region TODO:获取用户的所有权限
        List<String> permissionList = new ArrayList<>();
        permissionList.add("add");
        permissionList.add("del");
        permissionList.add("update");
        permissionList.add("query");
        permissionList.add("user:query");
        permissionList.add("user:edit");
        //endregion

        //用户的角色对应的所有权限，如果只使用角色定义访问权限，下面的行可以不要
        info.addStringPermissions(permissionList);
        return info;
    }

    /**
     * 登录认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //UsernamePasswordToken对象用来存放提交的登录信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        System.out.println("验证当前Subject时获取到token为：" + ReflectionToStringBuilder.toString(token, ToStringStyle.MULTI_LINE_STYLE));

        //region TODO:查出是否有此用户
        User user = userService.getUserByName(token.getUsername());
        if (user == null) {
            return null;
        }
        //endregion

        return new SimpleAuthenticationInfo(user.getName(), user.getPassword(), getName());
    }
}
