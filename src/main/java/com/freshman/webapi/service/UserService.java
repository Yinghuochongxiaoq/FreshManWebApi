package com.freshman.webapi.service;

import com.freshman.webapi.dao.UserMapper;
import com.freshman.webapi.pojo.User;
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
    public User getUser(String userName, String passWord) {
        return userMapper.loginCheck(userName, passWord);
    }

    public User getUserId(int id) {
        return userMapper.selectByPrimaryKey(id);
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
        User user = userMapper.selectByName(username);
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
