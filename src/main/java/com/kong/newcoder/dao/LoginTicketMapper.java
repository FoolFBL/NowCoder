package com.kong.newcoder.dao;

import com.kong.newcoder.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shijiu
 */
@Mapper
@Deprecated
public interface LoginTicketMapper {
    //新增登录凭证
    int insertLoginTicket(LoginTicket loginTicket);
    //查询登录凭证
    LoginTicket selectByTicket(String ticket);
    //修改状态
    int updataStatus(String ticket , int status);

}
