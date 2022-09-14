package com.kong.newcoder.controller;

import com.kong.newcoder.Service.FollowService;
import com.kong.newcoder.Service.LikeService;
import com.kong.newcoder.Service.UserService;
import com.kong.newcoder.annotation.LoginRequired;
import com.kong.newcoder.entity.User;
import com.kong.newcoder.util.CommunityConstant;
import com.kong.newcoder.util.CommunityUtil;
import com.kong.newcoder.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * @author shijiu
 */
@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    //上传路径
    @Value("${community.path.upload}")
    private String uploadPath;
    //域名
    @Value("${community.path.domain}")
    private String domain;
    //访问路径
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    //跳转页面
    @LoginRequired
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    //处理上传逻辑
    @LoginRequired
    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    //MultipartFile 从页面传进来的值
    public String  uploadHeader(MultipartFile headerImage, Model model){
        if(headerImage==null){
            model.addAttribute("error","您还没有选择图片");
            return "/site/setting";
        }
        //获取原始文件名
        String filename = headerImage.getOriginalFilename();
        //获取后缀名
        String suffix = filename.substring(filename.lastIndexOf("."));
        //后缀名为空
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","文件的格式不正确");
            return "/site/setting";
        }
        //生成随机文件名
        filename=CommunityUtil.generateUUID()+suffix;
        //确定文件存放的路径
        File dest = new File(uploadPath+"/"+filename);
        //用户上传文件内容写入要存放的文件
        try {
            //存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传文件失败"+e.getMessage());
            throw new RuntimeException("上传文件失败 服务器发生异常",e);
        }
        //更新当前用户头像路径(web)
       //http://localhost:8080/community/user/header/xxx.png
       User user = hostHolder.getUser();
        String headerUrl = domain+contextPath+"/user/header/"+filename;
        userService.updateHeader(user.getId(),headerUrl);
        return "redirect:/index";
    }


    //获取头像
    @RequestMapping(path = "/header/{filename}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("filename")String filename, HttpServletResponse response) throws IOException {
        //服务器存放路径
       filename = uploadPath+"/"+filename;
       //解析后缀
        String suffix = filename.substring(filename.lastIndexOf(".")+1);
        //响应图片
        response.setContentType("image/"+suffix);
        //字节流写入
        FileInputStream fis = new FileInputStream(filename);
        try {
              OutputStream os = response.getOutputStream();
             fis = new FileInputStream(filename);
                byte[] buffer = new byte[1024];
                int b = 0;
                while((b = fis.read(buffer))!=-1){
                    os.write(buffer,0,b);
                }
        } catch (IOException e) {
            logger.error("读取头像失败"+e.getMessage());
         }
      finally {
            fis.close();
        }
    }

    //修改密码
    @LoginRequired
    @RequestMapping(path = "/updatePassword" ,method = RequestMethod.POST)
    public String updatePassword(String oldPassword,String newPassword,String confrimPassword,Model model){
      //从当前状态中获取user
        User user = hostHolder.getUser();
        Map<String, Object> map = userService.updatePassword(user.getUsername(), oldPassword, newPassword, confrimPassword);
        //如果一切顺利  map为空
        if(map==null||map.isEmpty()){
            //传值
            model.addAttribute("msg","修改密码成功");
            //跳转页面 返回登录页面 重新登录
            //此时可能还需要退出原来登录状态 一会添加
            return "/site/login";
        }
        else{
            //刷新本页
            model.addAttribute("errorMsg",map.get("passwordMsg"));
            return "/site/setting";
        }
    }

    //个人主页
    @RequestMapping(path = "/profile/{userId}" , method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId")int userId,Model model){
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user",user);
        //点赞数量
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount",likeCount);

        //关注数量
        long followeeCount = followService.findFolloweeCount(userId, ENTITY_TYPE_USER);
        model.addAttribute("followeeCount",followeeCount);
//        //粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount",followerCount);
        //是否已关注
        boolean hasFollowed = false;
        if(hostHolder.getUser() != null){
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(),ENTITY_TYPE_USER,userId);
        }
        model.addAttribute("hasFollowed",hasFollowed);

        return "/site/profile";
    }




}
