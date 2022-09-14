package com.kong.newcoder;

import com.kong.newcoder.util.SensitiveFilter;
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
public class SensitiveTest {

    @Autowired
    private SensitiveFilter sensitiveFilterfilter;
    @Test
    public void testSensitiveFilter(){
        String text = "这里可以赌~博 可以嫖娼哈哈哈哈哈";
        String filter = sensitiveFilterfilter.filter(text);
        System.out.println(filter);
    }

}
