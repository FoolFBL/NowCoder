package com.kong.newcoder.dao;

import com.kong.newcoder.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author shijiu
 */
@Mapper
public interface CommentMapper {
    //根据实体来查 课程的评论 帖子的评论
    List<Comment>selectCommentsByEntity(int entityType,int entityId,int offset,int limit);
    //实体评论的一个数量
    int selectCountByEntity(int entityType,int entityId);
    //增加评论
    int insertComment(Comment comment);
    //根据id查一个Comment
    Comment selectCommentById(int id);





}
