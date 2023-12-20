package com.github.xszhangxiaocuo.entity.req.admin;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AdminArticlesPOSTReq {
    @JsonProperty("id")
    private int id;

    @JsonProperty("userId")
    private int userId;

    @JsonProperty("articleContent")
    private String articleContent;

    @JsonProperty("articleCover")
    private String articleCover;

    @JsonProperty("articleTitle")
    private String articleTitle;

    @JsonProperty("categoryId")
    private int categoryId;

    @JsonProperty("isTop")
    private byte isTop;

    @JsonProperty("isDraft")
    private byte isDraft;

    @JsonProperty("isDelete")
    private byte isDelete;

    @JsonProperty("tagIdList")
    private List<Integer> tagIdList;

    public String getArticleContent() {
        return articleContent;
    }

    public String getArticleCover() {
        return articleCover;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public byte getIsTop() {
        return isTop;
    }

    public byte getIsDraft() {
        return isDraft;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public void setArticleCover(String articleCover) {
        this.articleCover = articleCover;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setIsTop(byte isTop) {
        this.isTop = isTop;
    }

    public void setIsDraft(byte isDraft) {
        this.isDraft = isDraft;
    }

    public List<Integer> getTagIdList() {
        return tagIdList;
    }

    public void setTagIdList(List<Integer> tagIdList) {
        this.tagIdList = tagIdList;
    }

    public int getId() {
        return id;
    }

    public byte getIsDelete() {
        return isDelete;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIsDelete(byte isDelete) {
        this.isDelete = isDelete;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
