package com.kong.newcoder.controller.interceptor;

import com.kong.newcoder.annotation.LoginRequired;
import com.kong.newcoder.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author shijiu
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果拦截到的是一个方法 进行强制转换
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取拦截到方法的对象
            Method method = handlerMethod.getMethod();
             //取对应的注解
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            //如果此方法上有LoginRequired注解 并且未登录
            if(loginRequired!=null && hostHolder.getUser()==null){
                //重定向
                response.sendRedirect(request.getContextPath()+"/login");
                return false;
            }
        }
        return true;
    }
}
