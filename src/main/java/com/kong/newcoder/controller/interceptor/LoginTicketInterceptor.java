package com.kong.newcoder.controller.interceptor;

import com.kong.newcoder.Service.UserService;
import com.kong.newcoder.entity.LoginTicket;
import com.kong.newcoder.entity.User;
import com.kong.newcoder.util.CookieUtil;
import com.kong.newcoder.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author shijiu
 * 这是个拦截器
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoginTicketInterceptor.class);

    @Autowired
  private UserService userService;

  @Autowired
  private HostHolder hostHolder;
      @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

          logger.debug(response.toString());

        //从cookie中获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");

        if(ticket!=null){
            //查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);

            //检查凭证是否有效
            //凭证不为空 并且凭证未失效 且凭证的超时时间在现在之后
        if(loginTicket!=null &&loginTicket.getStatus()==0&&loginTicket.getExpired().after(new Date())){
           //根据凭证查询用户

            User user = userService.findUserById(loginTicket.getUserid());

            //在本次请求中持有用户
            //多线程隔离存对象
            hostHolder.setUsers(user);
            System.out.println(user);
            logger.debug(user+"user");
        }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
     logger.debug(String.valueOf(request.getRequestURL()));
          //从中获取user
        User user = hostHolder.getUser();

        if(user!=null&&modelAndView!=null) {
            modelAndView.addObject("loginUser", user);
            logger.debug(user.toString());
        }

//        System.out.println(user.toString()+"        ");
      }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //等拦截器结束 清掉数据
          hostHolder.clear();
    }
}
