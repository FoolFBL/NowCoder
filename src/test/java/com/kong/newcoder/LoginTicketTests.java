package com.kong.newcoder;

import com.kong.newcoder.dao.LoginTicketMapper;
import com.kong.newcoder.entity.LoginTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author shijiu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NewCoderApplication.class)
public class LoginTicketTests {

    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Test
    public void test(){
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserid(123);
        loginTicket.setTicket("ada");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date());
        int i = loginTicketMapper.insertLoginTicket(loginTicket);
        System.out.println(i);

        LoginTicket ada = loginTicketMapper.selectByTicket("ada");
        System.out.println(ada);

        int a = loginTicketMapper.updataStatus("ada", 1);
        System.out.println(a);
    }
}
