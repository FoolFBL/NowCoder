package com.kong.newcoder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author shijiu
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NewCoderApplication.class)
public class LoggerTest {
    private static final Logger logger = LoggerFactory.getLogger(LoggerTest.class);
    @Test
    public void testLogger(){
        System.out.println(logger.getName());
        logger.debug("debug log");
        logger.info("info log");
        logger.warn("warn log");
        System.out.println("ok");
        logger.error("error log");
    }
    @Test
    public void test(){

        int i = 5;
        int arr[]=new int[i];
        for (int i1 : arr) {
            System.out.println(i1);
        }
        return ;
    }

}
