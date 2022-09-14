package com.kong.newcoder.controller;
import com.alibaba.fastjson.JSONObject;
import com.kong.newcoder.Service.MessageService;
import com.kong.newcoder.Service.UserService;
import com.kong.newcoder.entity.Message;
import com.kong.newcoder.entity.Page;
import com.kong.newcoder.entity.User;
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
import org.springframework.web.util.HtmlUtils;

import java.util.*;

/**
 * @author shijiu
 */
@Controller
public class MessageController implements CommunityConstant {
    @Autowired
    private MessageService messageService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;

    //私信列表
    @RequestMapping(path = "/letter/list", method = RequestMethod.GET)
    public String getLetterList(Model model, Page page) {
//        Integer.valueOf("abc");
        User user = hostHolder.getUser();
        //分页信息
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCount(user.getId()));
        //会话列表
        List<Message> conversationList =
                messageService.findConversations(user.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", message);
                map.put("letterCount", messageService.findLetterCount(message.getConversationId()));
                map.put("unreadCount", messageService.findLetterUnreadCount(user.getId(), message.getConversationId()));
                int targetId = user.getId() == message.getFromId() ? message.getToId() : message.getFromId();
                map.put("target", userService.findUserById(targetId));
                conversations.add(map);
            }
        }
        model.addAttribute("conversations", conversations);
        //查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
//        try{
//            int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//        }
          int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);

        model.addAttribute("noticeUnreadCount",noticeUnreadCount);

        return "/site/letter";
    }

    //私信详情
    @RequestMapping(path = "/letter/detail/{conversationId}", method = RequestMethod.GET)
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Page page, Model model) {
        //分页信息
        page.setLimit(5);
        page.setPath("/letter/detail/" + conversationId);
        page.setRows(messageService.findLetterCount(conversationId));
        //私信列表
        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                Map<String, Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userService.findUserById(message.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters", letters);
        //查找私信目标
        model.addAttribute("target", getLetterTarget(conversationId));
        return "/site/letter-detail";
    }

    //私信目标
    private User getLetterTarget(String conversationId) {
        String[] s = conversationId.split("_");
        int d0 = Integer.parseInt(s[0]);
        int d1 = Integer.parseInt(s[1]);
        if (hostHolder.getUser().getId() == d0) {
            return userService.findUserById(d1);
        } else {
            return userService.findUserById(d0);
        }
    }
    @RequestMapping(path = "/letter/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendLetter(String toName, String content) {
//        Integer.valueOf("sass");
        //构造私信
        User target = userService.findUserByName(toName);
        System.out.println(target);
        if (target == null) {
            return CommunityUtil.getJSONString(1, "目标用户不存在");
        }
        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        if (message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setContent(content);
        message.setCreateTime(new Date());
        messageService.addMessage(message);
        return CommunityUtil.getJSONString(0);
    }


    //显示通知列表
    @RequestMapping(path = "/notice/list",method = RequestMethod.GET)
    public String getNoticeList(Model model){
        User user = hostHolder.getUser();
        //查询评论类的通知
        Message message = messageService.findLatestNotice(user.getId(), TOPIC_COMMENT);

        if(message!=null){
            Map<String,Object> messageVO = new HashMap<>();
            messageVO.put("message",message);
            //转化转义字符
            String content = HtmlUtils.htmlUnescape(message.getContent());
            HashMap<String,Object> data = JSONObject.parseObject(content, HashMap.class);
            messageVO.put("user",userService.findUserById(user.getId()));
            messageVO.put("entityType",data.get("entityType"));
            messageVO.put("entityId",data.get("entityId"));
            messageVO.put("postId",data.get("postId"));

            try {
                int count = messageService.findNoticeCount(user.getId(), TOPIC_COMMENT);
                messageVO.put("count",count);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

            int unread = messageService.findNoticeUnreadCount(user.getId(),TOPIC_COMMENT);
            messageVO.put("unread",unread);
            model.addAttribute("commentNotice",messageVO);
        }


        //查询点赞类的通知

        message = messageService.findLatestNotice(user.getId(), TOPIC_LIKE);
        if(message!=null){
            Map<String,Object> messageVO = new HashMap<>();

            messageVO.put("message",message);
            //转化转义字符
            String content = HtmlUtils.htmlUnescape(message.getContent());
            HashMap<String,Object> data = JSONObject.parseObject(content, HashMap.class);
            messageVO.put("user",userService.findUserById(user.getId()));
            messageVO.put("entityType",data.get("entityType"));
            messageVO.put("entityId",data.get("entityId"));
            messageVO.put("postId",data.get("postId"));
            int count = messageService.findNoticeCount(user.getId(), TOPIC_LIKE);
            messageVO.put("count",count);
            int unread = messageService.findNoticeUnreadCount(user.getId(),TOPIC_LIKE);
            messageVO.put("unread",unread);
            model.addAttribute("likeNotice",messageVO);
        }



        //查询关注类的通知
        message = messageService.findLatestNotice(user.getId(), TOPIC_FOLLOW);
        if(message!=null){
            Map<String,Object> messageVO = new HashMap<>();

            messageVO.put("message",message);
            //转化转义字符
            String content = HtmlUtils.htmlUnescape(message.getContent());
            HashMap<String,Object> data = JSONObject.parseObject(content, HashMap.class);
            messageVO.put("user",userService.findUserById(user.getId()));
            messageVO.put("entityType",data.get("entityType"));
            messageVO.put("entityId",data.get("entityId"));
            int count = messageService.findNoticeCount(user.getId(), TOPIC_FOLLOW);
            messageVO.put("count",count);
            int unread = messageService.findNoticeUnreadCount(user.getId(),TOPIC_FOLLOW);
            messageVO.put("unread",unread);
            model.addAttribute("followNotice",messageVO);
        }


        //查询未读消息总数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(),null);
        model.addAttribute("letterUnreadCount",letterUnreadCount);

        //查询未读通知数量
        int noticeUnreadCount = messageService.findNoticeUnreadCount(user.getId(), null);
        model.addAttribute("noticeUnreadCount",noticeUnreadCount);

        return "/site/notice";

    }




    @RequestMapping(path = "/notice/detail/{topic}",method = RequestMethod.GET)
    public String getNoticeDetail(@PathVariable("topic") String topic,Page page,Model model){
        User user = hostHolder.getUser();
        page.setLimit(5);
        page.setPath("/notice/detail/"+topic);
        page.setRows(messageService.findNoticeCount(user.getId(),topic));
        try {
            List<Message> noticeList = messageService.findNotices(user.getId(), topic, page.getOffset(), page.getLimit());
            List<Map<String,Object>>noticeVOList = new ArrayList<>();
            if(noticeList!=null){
                for(Message notice : noticeList){
                    Map<String,Object> map = new HashMap<>();
                    //通知
                    map.put("notice",notice);
                    //内容
                    String content = HtmlUtils.htmlUnescape(notice.getContent());
                    Map<String,Object>data = JSONObject.parseObject(content,HashMap.class);
                    map.put("user",userService.findUserById((Integer) data.get("userId")));
                    map.put("entityType",data.get("entityType"));
                    map.put("entityId",data.get("entityId"));
                    map.put("postId",data.get("postId"));
                    //通知的作者
                    map.put("fromUser",userService.findUserById(notice.getFromId()));
                    noticeVOList.add(map);
                }
                model.addAttribute("notices",noticeVOList);
                //设置已读
                List<Integer> ids = getLetterIds(noticeList);
                if(!ids.isEmpty()){
                    messageService.readMessage(ids);
                }

            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }



        return "/site/notice-detail";
    }


    private List<Integer> getLetterIds(List<Message> letterList) {
        List<Integer> ids = new ArrayList<>();

        if (letterList != null) {
            for (Message message : letterList) {
                if (hostHolder.getUser().getId() == message.getToId() && message.getStatus() == 0) {
                    ids.add(message.getId());
                }
            }
        }

        return ids;
    }

}
