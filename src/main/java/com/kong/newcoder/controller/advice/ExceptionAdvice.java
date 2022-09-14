package com.kong.newcoder.controller.advice;

import com.kong.newcoder.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author shijiu
 */
//只去扫描带有Controller注解的类
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);
    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("服务器发生异常"+e.getMessage());
        for (StackTraceElement element:e.getStackTrace()){
            logger.error(element.toString());
        }
        //同步请求 返回html
        //异步请求 返回json数据
        String header = request.getHeader("x-requested-with");
        if("XMLHttpRequest".equals(header)){
            //异步请求
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJSONString(1,"服务器异常"));
        }else{
            response.sendRedirect(request.getContextPath()+"/error");
        }
    }

}
