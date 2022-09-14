package com.kong.newcoder.entity;


import lombok.*;

import java.util.Date;

/**
 * @author shijiu
 */
//字段名要与数据库中字段名保持一致
@Data
@Getter
@Setter
public class User {

    private int id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private int type;
    private int status;
    private String activationCode;
    private String headerurl;
    private Date createTime;
}
