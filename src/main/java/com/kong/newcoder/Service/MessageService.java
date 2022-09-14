package com.kong.newcoder.Service;

import com.kong.newcoder.dao.MessageMapper;
import com.kong.newcoder.entity.Message;
import com.kong.newcoder.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author shijiu
 */
@Service
public class MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;
    public List<Message> findConversations(int userId,int offset,int limit){
        List<Message> messages = messageMapper.selectConversations(userId, offset, limit);
        return messages;
    }
    public int findConversationCount(int userId){
        return messageMapper.selectConversationCount(userId);
    }
    public List<Message> findLetters(String conversationId,int offset,int limit){
        return messageMapper.selectLetters(conversationId,offset,limit);
    }

    public int findLetterCount(String conversationId){
        return messageMapper.selectLetterCount(conversationId);
    }
    public int findLetterUnreadCount(int userId,String conversationId){
        return messageMapper.selectLetterUnreadCount(userId,conversationId);
    }

    public int addMessage(Message message){
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
       return messageMapper.insertMessage(message);
    }
    //将消息变为已读
    public int readMessage(List<Integer> ids){
        return messageMapper.updateStatus(ids,1);
    }


    //查某一个主题下面最新的一条通知
    public Message findLatestNotice(int userId,String topic){
        return messageMapper.selectLatestNotice(userId, topic);
    }

    //查某一个主题所包含的数量
    public int findNoticeCount(int userId,String topic){
        return messageMapper.selectNoticeCount(userId,topic);
    }


    //查询未读的通知数量
    public int findNoticeUnreadCount(int userId,String topic){
        return messageMapper.selectNoticeUnreadCount(userId, topic);
    }

    //查询某个主题所包含的通知列表
    public List<Message> findNotices(int userId,String topic,int offset,int limit){
        return messageMapper.selectNotices(userId, topic, offset, limit);
    }




}
