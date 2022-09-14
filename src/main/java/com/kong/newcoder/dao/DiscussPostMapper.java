package com.kong.newcoder.dao;

import com.kong.newcoder.entity.DiscussPost;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shijiu
 */

@Mapper
public interface DiscussPostMapper  {
    //起始行行号 每页显示多少数据
    //user_id 用户发的所有帖子 用来制作个人首页 现在用不到
    //当user_id=0 不拼接进去 不等于0时拼接进去 动态sql
    //从offset+1 开始 返回limit行 的user_id对应的讨论区帖子
    List<DiscussPost>selectDiscussPosts(int userid,int offset,int limit);

    //查询表中一共有多少个帖子数据 这里的user_id是为了后面展示个人页做准备
    //当user_id=0 不拼接进去 不等于0时拼接进去 动态sql
    ////需要动态拼接sql 并且只有一个参数 则必须加上param给参数起个别名
    //查询此user_id对应的数量
    int selectDiscussPostRows(@Param("userid") int userid);

    List<DiscussPost>selectTest(@Param("userid") int userid);

    //增加帖子
    int insertDiscussPost(DiscussPost discussPost);

    //显示帖子
    DiscussPost selectDiscussPostById(int id);
    //插入评论时 更新帖子的评论数量
    int updateCommentCount(int id,int commentCount);


    //查询所有帖子
    List<DiscussPost> selectAllDiscussPosts();


}
