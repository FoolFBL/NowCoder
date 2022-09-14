package com.kong.newcoder;

import com.kong.newcoder.dao.DiscussPostMapper;
import com.kong.newcoder.dao.MessageMapper;
import com.kong.newcoder.dao.UserMapper;
import com.kong.newcoder.entity.DiscussPost;
import com.kong.newcoder.entity.Message;
import com.kong.newcoder.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jws.soap.SOAPBinding;
import java.util.Date;
import java.util.List;

/**
 * @author shijiu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NewCoderApplication.class)
public class MapperTests {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private MessageMapper messageMapper;


    @Test
    public void testSelectLetters() {
        List<Message> messages = messageMapper.selectConversations(111, 0, 20);
        for (Message message : messages) {
            System.out.println(message);
        }

        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        List<Message> messages1 = messageMapper.selectLetters("111_112", 0, 10);
        for (Message message : messages1) {
            System.out.println(message);
        }

        count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);

         count = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(count);



    }


    @Test
    public void testselect() {
        User user = userMapper.selectById(101);
        System.out.println(user);
        User user1 = userMapper.selectByName("test");
        System.out.println(user1);
        User user2 = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user2);
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("14436@qq.com");
        user.setHeaderurl("http://www.nowcoder.com/666.png");
        user.setCreateTime(new Date());
        int i = userMapper.insertUser(user);
        System.out.println(i);
    }

    @Test
    public void testUpdateUser() {
        // int i = userMapper.updateStatus();
        int i = userMapper.updateStatus(150, 2);
        System.out.println(i);
        int i1 = userMapper.updatePassword(150, "http");
        System.out.println(i1);
        int i2 = userMapper.updateHeader(150, "http://www.nowcoder.com/666.png");
        System.out.println(i2);
    }

    @Test
    public void testSelectPosts() {
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(101, 0, 10);
        for (DiscussPost discussPost : discussPosts) {
            System.out.println(discussPost);
            System.out.println(discussPost.getUserid());
        }
        int i = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(i);
    }


    @Test
    public void test() {
        List<DiscussPost> list = discussPostMapper.selectTest(102);
        for (DiscussPost discussPost : list) {
            System.out.println(discussPost);
        }
    }


}
