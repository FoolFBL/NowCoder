package com.kong.newcoder.Service;

import com.kong.newcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author shijiu
 */
@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    //点赞
    public void like(int userId,int entityType,int entityId,int entityUserId){
    redisTemplate.execute(new SessionCallback() {
        @Override
        public Object execute(RedisOperations operations) throws DataAccessException {
            String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
            String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);
            //是否已经存在
            boolean isMember = operations.opsForSet().isMember(entityLikeKey, userId);
            //开启事务
            operations.multi();
            if(isMember){
                operations.opsForSet().remove(entityLikeKey,userId);
                operations.opsForValue().decrement(userLikeKey);
            }else{
                operations.opsForSet().add(entityLikeKey,userId);
                operations.opsForValue().increment(userLikeKey);
            }
            //提交事务
            return operations.exec();
        }
    });
    }

    //查询每个用户获得的赞

    public int findUserLikeCount(int userId){
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 :count.intValue();
    }

    //查询某实体点赞的数量
    public Long findEntityLikeCount(int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    //查询某人对某实体的点赞状态
    public int findEntityLikeStatus(int userId,int entityType,int entityId){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        Boolean member = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        if(member==true){
            return 1;
        }else {
            return 0;
        }
    }



}
