package com.kong.newcoder;

import com.kong.newcoder.Service.DemoService;
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
public class TransactionTests {

    @Autowired
    private DemoService demoService;
    @Test
    public void test(){
        Object save = demoService.save2();
        System.out.println(save);
    }
}
