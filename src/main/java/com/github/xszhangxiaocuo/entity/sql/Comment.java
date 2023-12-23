package com.github.xszhangxiaocuo.entity.sql;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class Comment extends Universal{
    public static String arrticleIdName = "article_id";
    public static String userIdName = "user_id";
    public static String commentContentName = "comment_content";
    public static String isDeleteName = "is_delete";

    @JsonProperty("articleId")
    private int arrticleId;//文章id

    @JsonProperty("userId")
    private int userId;//用户id

    @JsonProperty("commentContent")
    private String commentContent;//评论内容

    @JsonProperty("isDelete")
    private byte isDelete;//是否删除

    @JsonProperty("nickname")
    private String nickname;//用户昵称

    @JsonProperty("avatar")
    private String avatar;//用户头像

    @JsonProperty("articleTitle")
    private String articleTitle;//文章标题

    public Comment(){}

    public Comment(Timestamp now){
        setCreateTime(now);
    }

    public int getArrticleId() {
        return arrticleId;
    }

    public void setArrticleId(int arrticleId) {
        this.arrticleId = arrticleId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(byte isDelete) {
        this.isDelete = isDelete;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }
}
