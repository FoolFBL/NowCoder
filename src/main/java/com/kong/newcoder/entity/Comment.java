package com.kong.newcoder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author shijiu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Comment {
     private int id;
     //评论的发布者
     private int userId;
     //评论类型 可以是对帖子也可以是对人
     private int entityType;
     //对哪个帖子进行回复
     private int entityId;
     //回复谁
     private int targetId;
     //内容
    private String content;
    //状态 0表示正常 1表示禁用
    private int status;
    //创建时间
    private Date createTime;

}
