package com.freshman.webapi.service;

import com.freshman.webapi.dao.UserMapper;
import com.freshman.webapi.pojo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    /**
     * 根据用户名密码查询用户信息
     * @param userName
     * @param passWord
     * @return
     */
    public User getUser(String userName, String passWord) {
        return userMapper.loginCheck(userName, passWord);
    }

    /**
     * 根据用户id获取用户信息
     * @param id
     * @return
     */
    public User getUserId(int id) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据用户名称获取用户信息
     * @param name
     * @return
     */
    public User getUserByName(String name){
        if(StringUtils.isBlank(name)){
            return null;
        }
        return userMapper.selectByName(name);
    }

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取用户信息
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " not found.");
        }
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getName());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        user.setAuthorities(authorities);
        return user;
    }
}
