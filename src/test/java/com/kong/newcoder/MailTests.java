package com.kong.newcoder;

import com.kong.newcoder.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.ByteBuffer;

/**
 * @author shijiu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NewCoderApplication.class)
public class MailTests {
    @Autowired
    private MailClient mailClient;
    @Autowired
//   注入Thymeleaf核心类
   private TemplateEngine templateEngine;
    @Test
    public void testMail(){
        mailClient.sendMail("1443687642@qq.com","Test","cheer up my bro");
    }
    @Test
    public void testHtmlEmail(){
        Context context = new Context();
        //设置参数
        context.setVariable("username","kong");
        //生成网页
        String content = templateEngine.process("/mail/MailDemo",context);
        System.out.println(content);
        mailClient.sendMail("1443687642@qq.com","HTML",content);



    }

}
