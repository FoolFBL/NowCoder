package com.kong.newcoder;

import com.kong.newcoder.dao.CommentMapper;
import com.kong.newcoder.dao.MessageMapper;
import com.kong.newcoder.entity.Comment;
import com.kong.newcoder.entity.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * @author shijiu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NewCoderApplication.class)
public class CommentTest {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void test() {
        List<Comment> comments = commentMapper.selectCommentsByEntity(1, 228, 0, 4);
        for (Comment comment : comments) {
            System.out.println(comment);
        }
    }

    @Test
    public void test2() {
        int i = commentMapper.selectCountByEntity(1, 228);
        System.out.println(i);
    }

    @Test
    public void test3() {
        Message message = new Message();
        message.setFromId(111);
        message.setToId(112);
        message.setConversationId("111_112");
        message.setContent("sas");
        message.setCreateTime(new Date());
        message.setId(1);
        message.setStatus(0);
        try {
            int i = messageMapper.insertMessage(message);
        }catch (Exception e){
            System.out.println("kkkkkkk"+e.getMessage());
        }
    }


}
