package com.freshman.webapi.service;

import com.freshman.webapi.dao.UserMapper;
import com.freshman.webapi.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public User getUser(String userName, String passWord) {
        return userMapper.loginCheck(userName,passWord);
    }

    public User getUserId(int id){
        return userMapper.selectByPrimaryKey(id);
    }

    @Autowired
    private UserMapper userMapper;
}
