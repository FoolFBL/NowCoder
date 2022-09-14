package com.kong.newcoder.Service;

import com.kong.newcoder.dao.DiscussPostMapper;
import com.kong.newcoder.dao.UserMapper;
import com.kong.newcoder.entity.DiscussPost;
import com.kong.newcoder.entity.User;
import com.kong.newcoder.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;

/**
 * @author shijiu
 */
@Service
public class DemoService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private TransactionTemplate template;
    //隔离级别 和 传播机制
    //REQUIRED 支持当前事务  A 调 B 听A的 若A不存在则创建新事务
    //REQUIRES_NEW 创建一个新的事务 并且暂停当前事务(外部事务)
    //NESTED 如果当前存在事务(外部事务) 则嵌套在该事务中执行（独立的提交和回滚）若不存在 和REQUIRED一致
    //在方法中可能存在嵌套调用的情况 两个方法可能都有隔离级别 我们用传播机制选择使用哪个方法的隔离级别

    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public Object save(){
        //新增用户
        User user = new User();
        user.setUsername("alpha");
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5("123"+user.getSalt()));
        user.setEmail("@qq.com");
        user.setHeaderurl("http://image.nowcoder.com/head/99t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        //新增帖子
        DiscussPost post = new DiscussPost();
        post.setUserid(user.getId());
        post.setTitle("hello");
        post.setContent("shshhs");
        post.setCreatetime(new Date());
        discussPostMapper.insertDiscussPost(post);
        Integer.valueOf("abc");
        return "ok";
    }

    public Object save2(){
        //声明隔离级别
        template.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        //设置传播方式
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        return template.execute(new TransactionCallback<Object>() {

            @Override
            public Object doInTransaction(TransactionStatus status) {
                //新增用户
                User user = new User();
                user.setUsername("alpha");
                user.setSalt(CommunityUtil.generateUUID().substring(0,5));
                user.setPassword(CommunityUtil.md5("123"+user.getSalt()));
                user.setEmail("@qq.com");
                user.setHeaderurl("http://image.nowcoder.com/head/99t.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);
                //新增帖子
                DiscussPost post = new DiscussPost();
                post.setUserid(user.getId());
                post.setTitle("hello");
                post.setContent("shshhs");
                post.setCreatetime(new Date());
                discussPostMapper.insertDiscussPost(post);
                Integer.valueOf("abc");
                return "ok";
            }
        });



    }



}
