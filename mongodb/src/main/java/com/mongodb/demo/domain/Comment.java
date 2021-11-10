package com.mongodb.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "comment")
public class Comment implements Serializable {

    @Id  //主键标识，该属性的值对应mongodb的主键"_id"，如果该属性名就叫“id”,则该注解可以省略，否则必须写
    private String id;

    @Field("content") // 该属性对应mongodb的字段的名字，如果一直可以省略
    private String content;
    private Date publishTime;

    private String userId;
    private String nickName;
    private LocalDateTime createDateTime;
    private Integer likeNum;
    private Integer replyNum;
    private String state;
    private String parentId;
    private String articleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(LocalDateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(Integer replyNum) {
        this.replyNum = replyNum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", publishTime=" + publishTime +
                ", userId='" + userId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", createDateTime=" + createDateTime +
                ", likeNum=" + likeNum +
                ", replyNum=" + replyNum +
                ", state='" + state + '\'' +
                ", parentId='" + parentId + '\'' +
                ", articleId='" + articleId + '\'' +
                '}';
    }
}
