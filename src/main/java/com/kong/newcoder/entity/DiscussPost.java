package com.kong.newcoder.entity;

import lombok.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @author shijiu
 */
//帖子
@Data

@Document(indexName = "discusspost")
public class DiscussPost {
    //    @Id
    private int id;
    //    @Field(type = FieldType.Integer)
    private int userid;
    //互联网校招
//    @Field(type = FieldType.Text,analyzer = "ik_max_word" ,searchAnalyzer = "ik_smart" )
    private String title;
    //    @Field(type = FieldType.Text,analyzer = "ik_max_word" ,searchAnalyzer = "ik_smart" )
    private String content;
    //    @Field(type = FieldType.Integer)
    private int type;//'0-普通; 1-置顶;',
    //    @Field(type = FieldType.Integer)
    private int status; //'0-正常; 1-精华; 2-拉黑;',
    //    @Field(type = FieldType.Date)
    private Date createtime;//创建时间
    //帖子评论数量
//    @Field(type = FieldType.Integer)
    private int commentCount;
    //帖子分数
//    @Field(type = FieldType.Double)
    private double score;

    public DiscussPost() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "DiscussPost{" +
                "id=" + id +
                ", userid=" + userid +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", createtime=" + createtime +
                ", commentCount=" + commentCount +
                ", score=" + score +
                '}';
    }
}
