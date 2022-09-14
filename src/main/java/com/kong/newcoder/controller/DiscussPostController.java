package com.kong.newcoder.controller;

import com.kong.newcoder.Service.CommentService;
import com.kong.newcoder.Service.DiscussPostService;
import com.kong.newcoder.Service.LikeService;
import com.kong.newcoder.Service.UserService;
import com.kong.newcoder.entity.*;
import com.kong.newcoder.event.EventProducer;
import com.kong.newcoder.util.CommunityConstant;
import com.kong.newcoder.util.CommunityUtil;
import com.kong.newcoder.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author shijiu
 */
@Controller
@RequestMapping("/post")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private EventProducer eventProducer;
    @RequestMapping(path = "/addPost",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title , String content){
        //验证是否登录
        User user = hostHolder.getUser();
        if(user==null){
            return CommunityUtil.getJSONString(403,"您还未登录");
        }
        DiscussPost post = new DiscussPost();
        post.setUserid(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreatetime(new Date());
        post.setCommentCount(0);
        post.setStatus(0);
        post.setScore(0);
        discussPostService.addDiscussPost(post);
        //触发发帖事件
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(user.getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(post.getId());
        eventProducer.fireEvent(event);

        //报错的情况 以后统一处理
        return CommunityUtil.getJSONString(0,"发布成功");
    }

    @RequestMapping(path = "/detail/{discussPostId}",method = RequestMethod.GET)
    //@PathVariable从路径中取参
    public String getDiscussPost(@PathVariable("discussPostId")int discussPostId, Model model, Page page){
        //查询帖子
        DiscussPost discussPost = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("discussPost",discussPost);
        //可以关联查询也可以分两次查询 这里采用第二中方法 虽然慢一点 可以用redis来做优化 把常用的数据存入内存
        User user = userService.findUserById(discussPost.getUserid());
        model.addAttribute("user",user);
        //点赞数量
        Long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount",likeCount);
        //点赞状态
        int entityLikeStatus = hostHolder.getUser() == null?0:
                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeStatus",entityLikeStatus);

        //还有回复 后面补充
        //查评论的分页信息
        //每页显示五条
        page.setLimit(5);
        //路径
        page.setPath("/post/detail/"+discussPostId);
        //一共多少行
        page.setRows(discussPost.getCommentCount());
        //帖子评论
        List<Comment> commentsList = commentService.findCommentsByEntity
                (ENTITY_TYPE_POST, discussPost.getId(), page.getOffset(), page.getLimit());
        List<Map<String,Object>> commentVoList=new ArrayList<>();
        if(commentsList!=null){
            for(Comment comment:commentsList){
                //一级帖子评论 向其中添加评论和评论的作者
                Map<String,Object>commentVo = new HashMap<>();
                commentVo.put("comment",comment);
                commentVo.put("user",userService.findUserById(comment.getUserId()));
                //点赞数量
                 likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, comment.getId());
                commentVo.put("likeCount",likeCount);
                //点赞状态
                 entityLikeStatus = hostHolder.getUser() == null?0: likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
                commentVo.put("likeStatus",entityLikeStatus);
                //二级帖子评论 对评论的回复
                //查询回复列表
                List<Comment> replyList = commentService.findCommentsByEntity
                        (ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String,Object>>replyVoList = new ArrayList<>();
                if(replyList!=null){
                    for(Comment reply:replyList){
                        Map<String,Object>replyVo = new HashMap<>();
                        replyVo.put("reply",reply);
                        //回复的作者
                        replyVo.put("user",userService.findUserById(reply.getUserId()));
                        //回复的目标用户
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target",target);
                        //点赞数量
                         likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, reply.getId());
                        replyVo.put("likeCount",likeCount);
                        //点赞状态
                         entityLikeStatus = hostHolder.getUser() == null?0: likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, reply.getId());
                        replyVo.put("likeStatus",entityLikeStatus);
                        replyVoList.add(replyVo);
                    }
                }
                //回复的数量
                int replyCounts = commentService.findCommentCounts(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount",replyCounts);
                commentVo.put("replys",replyVoList);
                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments",commentVoList);
        return "/site/discuss-detail";
    }


}
