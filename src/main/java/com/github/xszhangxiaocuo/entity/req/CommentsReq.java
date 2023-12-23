package com.github.xszhangxiaocuo.entity.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class CommentsReq {
    @JsonProperty("userId")//指定 JSON 中的字段名称
    private int userID;

    @JsonProperty("articleId")//指定 JSON 中的字段名称
    private int articleId;

    @JsonProperty("commentContent")
    private String commentContent;//评论内容

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }
}
