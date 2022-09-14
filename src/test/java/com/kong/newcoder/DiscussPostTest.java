package com.kong.newcoder;

import com.kong.newcoder.dao.DiscussPostMapper;
import com.kong.newcoder.entity.DiscussPost;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author shijiu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NewCoderApplication.class)
public class DiscussPostTest {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Test
    public void test(){
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserid(2);
        discussPost.setTitle("123");
        int i = discussPostMapper.insertDiscussPost(discussPost);
    }


}
