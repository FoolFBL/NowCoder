package com.kong.newcoder.dao;

import com.kong.newcoder.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author shijiu
 */
@Mapper
public interface MessageMapper {
    //查询当前用户的会话列表 针对每个会话只返回一条最新的私信  分页
    List<Message> selectConversations(int userId,int offset,int limit);
    //查询当前用户的会话数量
    int selectConversationCount(int userId);
    //查询某个会话所包含的私信列表.
    List<Message> selectLetters(String conversationId,int offset,int limit);
    //查询某个会话所包含的私信数量
    int selectLetterCount(String conversationId);
    //查询未读私信数量 单个会话和所有会话  动态拼接sql
    int selectLetterUnreadCount(int userId, String conversationId);
    //发送私信 新增消息
    int insertMessage(Message message);
    //修改信息状态   将未读消息设置为已读信息
    int updateStatus(List<Integer>ids,int status);

    //查某一个主题下面最新的一条通知
    Message selectLatestNotice(int userId,String topic);
    //查询某个主题所包含的数量
    int selectNoticeCount(int userId,String topic);
    //查询未读的通知的数量
    int selectNoticeUnreadCount(int userId,String topic);

    //查询某个主题所包含的通知列表
    List<Message> selectNotices(int userId,String topic,int offset,int limit);


}
