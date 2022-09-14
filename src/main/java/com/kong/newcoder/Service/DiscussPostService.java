package com.kong.newcoder.Service;

import com.kong.newcoder.dao.DiscussPostMapper;
import com.kong.newcoder.entity.DiscussPost;
import com.kong.newcoder.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author shijiu
 */
@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<DiscussPost> findDiscussPosts(int userid,int offset,int limit){
        return discussPostMapper.selectDiscussPosts(userid,offset,limit);
    }

    public int findDiscussPostRows(int userid){
        return discussPostMapper.selectDiscussPostRows(userid);
    }

    //发布帖子
    public int addDiscussPost(DiscussPost discussPost){
        if(discussPost==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //title content需要过滤敏感词 去除标签
        //先转义 html标签 让它作为纯文本出现 比如用户输入<p>123</p>
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        //过滤敏感词
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));

        return discussPostMapper.insertDiscussPost(discussPost);
    }


    //显示帖子详情
    public DiscussPost findDiscussPostById(int id){
        return discussPostMapper.selectDiscussPostById(id);
    }

    //插入评论时更新本帖评论数量
    public int updateCommentCount(int id,int commentCount){
        return discussPostMapper.updateCommentCount(id,commentCount);
    }


}
