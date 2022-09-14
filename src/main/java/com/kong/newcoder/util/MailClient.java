package com.kong.newcoder.util;

import lombok.extern.apachecommons.CommonsLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author shijiu
 */
@Component
//邮件客户端工具类
public class MailClient {
    private static  final Logger logger = LoggerFactory.getLogger(MailClient.class);
    @Autowired
    private JavaMailSender mailSender;
    //注入发邮件人的名称
    @Value("${spring.mail.username}")
    private String form;
    //发给谁 邮件主题 邮件内容
    public void sendMail(String to,String subject,String content){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(form);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,false);
            mailSender.send(helper.getMimeMessage());
        } catch (MessagingException e) {
            logger.error("发送邮件失败"+e.getMessage());
        }
    }


}
