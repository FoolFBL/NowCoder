package com.kong.newcoder.controller;

import com.kong.newcoder.Service.DiscussPostService;
import com.kong.newcoder.Service.LikeService;
import com.kong.newcoder.Service.UserService;
import com.kong.newcoder.entity.DiscussPost;
import com.kong.newcoder.entity.Page;
import com.kong.newcoder.entity.User;
import com.kong.newcoder.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shijiu
 */
@Controller
public class HomeController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;
    //路径index
    @CrossOrigin
    @RequestMapping("/index")
    public String getIndexPage(Model model, Page page){
        //查询全部 动态拼接
        //方法调用之前 SpringMVC会自动实例化model和page 而且会将page注入给model
        //所以在Thymeleaf中可以直接访问Page对象中的数据
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");
        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());

        //map集合
        List<Map<String,Object>>discussPosts = new ArrayList<>();
        if(list!=null){
            for(DiscussPost post : list){
                Map<String,Object> map = new HashMap<>();
                //将DiscussPost对象添加进map中
                map.put("post",post);
              User user = userService.findUserById(post.getUserid());
                //将user对象添加进map中
                map.put("user",user);

                Long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount",likeCount);
                //将map集合数组加入discussPosts集合中
                discussPosts.add(map);
            }
        }
        //传到前端的数据
        model.addAttribute("discussPosts",discussPosts);
        return "index";
    }

    @RequestMapping(path = "/error",method = RequestMethod.GET)
    public String getErrorPage(){
        return "/error/500";
    }


}
