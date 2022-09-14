package com.kong.newcoder.controller;
import com.kong.newcoder.Service.LikeService;
import com.kong.newcoder.entity.Event;
import com.kong.newcoder.entity.User;
import com.kong.newcoder.event.EventProducer;
import com.kong.newcoder.util.CommunityConstant;
import com.kong.newcoder.util.CommunityUtil;
import com.kong.newcoder.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shijiu
 */
//点赞
@Controller
public class LikeController implements CommunityConstant {

    @Autowired
    private LikeService likeService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private EventProducer eventProducer;

    @ResponseBody
    @PostMapping(path = "/like")
    public String like(int entityType,int entityId,int entityUserId,int postId){
        User user = hostHolder.getUser();
        //点赞
        likeService.like(user.getId(), entityType,entityId,entityUserId);

        //数量
        long likeCount = likeService.findEntityLikeCount(entityType,entityId);
        //状态
        int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType,entityId);

        Map<String,Object> map = new HashMap<>();
        map.put("likeCount",likeCount);
        map.put("likeStatus",likeStatus);


        //触发点赞事件 取消不触发
        if(likeStatus==1){
            Event event = new Event()
                    .setTopic(TOPIC_LIKE)
                    .setUserId(hostHolder.getUser().getId())
                    .setEntityType(entityType)
                    .setEntityId(entityId)
                    .setEntityUserId(entityUserId)
                    .setData("postId",postId);
            eventProducer.fireEvent(event);
        }


        return CommunityUtil.getJSONString(0,null,map);
    }
}
