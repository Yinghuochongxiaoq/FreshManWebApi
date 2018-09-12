package com.freshman.webapi.dao;


import com.freshman.webapi.pojo.User;
import org.omg.CORBA.UserException;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    User selectByName(String name);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User loginCheck(String username, String passWord);
}