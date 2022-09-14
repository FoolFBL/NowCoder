package com.kong.newcoder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author shijiu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = NewCoderApplication.class)
public class RedisTests {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Test
    public void testString() {
        String redisKey = "test:count";
        redisTemplate.opsForValue().set(redisKey, 1);
        System.out.println("取到的值" + redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println("增加后的值" + redisTemplate.opsForValue().get(redisKey));
    }

    @Test
    public void testHash(){
        String redisKey = "test:user";
        redisTemplate.opsForHash().put(redisKey,"id",1);
        redisTemplate.opsForHash().put(redisKey,"username","zhangsan");
        System.out.println(redisTemplate.opsForHash().get(redisKey,"id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey,"username"));
    }

    //列表
    @Test
    public void testLists(){
        String redisKey = "test:ids";
        redisTemplate.opsForList().leftPush(redisKey,101);
        redisTemplate.opsForList().leftPush(redisKey,102);
        redisTemplate.opsForList().leftPush(redisKey,103);
        System.out.println(redisTemplate.opsForList().size(redisKey));
        System.out.println(redisTemplate.opsForList().index(redisKey,0));
        System.out.println(redisTemplate.opsForList().range(redisKey,0,2));
        redisTemplate.opsForList().leftPop(redisKey);
        System.out.println(redisTemplate.opsForList().size(redisKey));
    }
    //集合
    @Test
    public void testSets(){
        String redisKey = "test:teacher";
        redisTemplate.opsForSet().add(redisKey,"刘备","关羽","张飞");
        System.out.println(redisTemplate.opsForSet().size(redisKey));
        //随便弹出一个值
        redisTemplate.opsForSet().pop(redisKey);
        System.out.println(redisTemplate.opsForSet().members(redisKey));
    }

    //有序集合
    @Test
    public void testSortedSets(){
        String redisKey = "test:students";
        redisTemplate.opsForZSet().add(redisKey,"唐曾1",80);
        redisTemplate.opsForZSet().add(redisKey,"唐曾2",90);
        redisTemplate.opsForZSet().add(redisKey,"唐曾3",1000);
        redisTemplate.opsForZSet().add(redisKey,"唐曾4",50);
        redisTemplate.opsForZSet().add(redisKey,"唐曾5",60);
        System.out.println(redisTemplate.opsForZSet().zCard(redisKey));
        System.out.println(redisTemplate.opsForZSet().score(redisKey,"唐曾2"));
        System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey,"唐曾2"));
        System.out.println(redisTemplate.opsForZSet().reverseRange(redisKey,0,2));

    }

    @Test
    public void testKeys() throws InterruptedException {
        //删除
        redisTemplate.delete("test:user");
        //是否有这个对象
        Boolean alive = redisTemplate.hasKey("test:user");
        System.out.println(alive);
        //过期时间
        redisTemplate.expire("test:students",10, TimeUnit.SECONDS);
        Thread.sleep(1000);
        System.out.println(redisTemplate.hasKey("test:students"));
    }

    //多次访问同一个key
    @Test
    public void testBoundOperations(){
        String redisKey = "test:count";
        //简化步骤相当于 语法糖
        BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);
        operations.increment();
    }

    //编程式事务
   @Test
   public void testTransactional(){
       Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public  Object execute(RedisOperations operations) throws DataAccessException {
               String redisKey = "test:tx";
               //启动事务
               operations.multi();
               operations.opsForSet().add(redisKey,"zhangsan");
                operations.opsForSet().add(redisKey,"lisi");
                operations.opsForSet().add(redisKey,"zhangsan");
                System.out.println(operations.opsForSet().members(redisKey));

               //提交事务
               return operations.exec();

            }
        });
       System.out.println(obj);
   }




}
