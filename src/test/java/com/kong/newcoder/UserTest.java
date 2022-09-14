package com.kong.newcoder;

import com.kong.newcoder.dao.UserMapper;
import com.kong.newcoder.entity.User;
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
public class UserTest {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void updatePasswordTest(){
        int i = userMapper.updatePassword(162, "123456");
        return ;
    }

    @Test
    public void selectByid(){
        User user = userMapper.selectByName("王老三");
//        User user = userMapper.selectById(1);
    System.out.println(user);
    }
}
