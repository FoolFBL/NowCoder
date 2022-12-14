package com.kong.newcoder.controller;

import com.google.code.kaptcha.Producer;
import com.kong.newcoder.Service.UserService;
import com.kong.newcoder.entity.User;
import com.kong.newcoder.util.CommunityConstant;
import com.kong.newcoder.util.CommunityUtil;
import com.kong.newcoder.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author shijiu
 */
@Controller
public class LoginController implements CommunityConstant {


    private static Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(path = "login", method = RequestMethod.GET)
    public String getLoginPage() {
        System.out.println("--------------------------------------------------------------------");
        return "/site/login";
    }


    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        //?????????????????????
        System.out.println();

        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "???????????? ?????????????????????????????????????????????????????? ??????????????????");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

    @RequestMapping(path = "/activation/{userid}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userid") int userid, @PathVariable("code") String code) {
        int result = userService.activation(userid, code);
        if (result == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "???????????? ???????????????????????????????????????~");
            model.addAttribute("target", "/login");
        } else if (result == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "??????????????? ???????????????????????????");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "???????????? ?????????????????? ??????????????????~");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";

    }


    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response, HttpSession session) {
        //???????????????
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);
        //??????????????????session
        //session.setAttribute("kaptcha", text);
        //??????????????? ????????????????????????????????????
        String kaptchaOwner = CommunityUtil.generateUUID();
        Cookie cookie = new Cookie("kaptchaOwner", kaptchaOwner);
        cookie.setMaxAge(60);
        cookie.setPath(contextPath);
        response.addCookie(cookie);
        //??????????????????redis
        String kaptchaKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
        redisTemplate.opsForValue().set(kaptchaKey, text, 60, TimeUnit.SECONDS);

        //???????????????????????????
        response.setContentType("image/png");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            logger.error("?????????????????????" + e.getMessage());
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean rememeberme, Model model,
            /* HttpSession session,*/ HttpServletResponse response,
                        @CookieValue("kaptchaOwner") String kaptchaOwner) {
//        String kaptcha = (String) session.getAttribute("kaptcha");
        String kaptcha = null;
        //?????????????????????
        if(StringUtils.isNotBlank(kaptchaOwner)){
            String kaptchaKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            kaptcha= (String) redisTemplate.opsForValue().get(kaptchaKey);
        }

        //???????????????????????????????????????
        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "??????????????????");
            return "/site/login";
        }
        //??????????????????
        int expiredSeconds = rememeberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        //??????
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }

    @RequestMapping(path = "/logOut", method = RequestMethod.GET)
    public String logOut(@CookieValue("ticket") String ticket) {
        userService.logOut(ticket);
        SecurityContextHolder.clearContext();
        return "redirect:/login";
    }
}