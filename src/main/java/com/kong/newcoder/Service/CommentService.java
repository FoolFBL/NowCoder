package com.kong.newcoder.Service;

import com.kong.newcoder.dao.CommentMapper;
import com.kong.newcoder.entity.Comment;
import com.kong.newcoder.util.CommunityConstant;
import com.kong.newcoder.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author shijiu
 */
@Service
public class CommentService implements CommunityConstant{
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;
    @Autowired
    private DiscussPostService discussPostService;

    public List<Comment> findCommentsByEntity(int entityType,int entityId,int offset,int limit){
        return commentMapper.selectCommentsByEntity(entityType,entityId,offset,limit);
    }

    public int findCommentCounts(int entityType,int entityId){
        return commentMapper.selectCountByEntity(entityType,entityId);
    }
    //事务管理 两次增删改  要么全部成功 要么全部失败
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int addComment(Comment comment){
        //过滤敏感词
        if(comment==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //过滤html标签
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        //过滤敏感词
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        //添加评论
        int rows = commentMapper.insertComment(comment);
        //更新帖子评论数量
        if(comment.getEntityType()== ENTITY_TYPE_POST)
        {
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(), count);
        }
        return rows;
    }

    //根据id查帖子

    public Comment findCommentById(int id){
        return commentMapper.selectCommentById(id);
    }


}
