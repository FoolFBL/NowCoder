package com.kong.newcoder.dao;

import com.kong.newcoder.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author shijiu
 */
@Mapper
public interface UserMapper {
    User selectById(int id);

    User selectByName(String name);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id,int status);

    int updateHeader(int id,String header_url);

    int updatePassword(int id,String password);
}

