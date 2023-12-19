package com.github.xszhangxiaocuo.entity.sql;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class Article extends Universal{
    public static String updateTimeName = "update_time";
    public static String userIdName = "user_id";
    public static String categoryIdName = "category_id";
    public static String articleCoverName = "article_cover";
    public static String articleTitleName = "article_title";
    public static String articleContentName = "article_content";
    public static String isTopName = "is_top";
    public static String isDraftName = "is_draft";
    public static String isDeleteName = "is_delete";
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("updateTime")
    private Timestamp updateTime;//更新时间

    @JsonProperty("userId")
    private int userId;//作者id

    @JsonProperty("categoryId")
    private int categoryId;//文章分类

    @JsonProperty("articleCover")
    private String articleCover;//文章缩略图
    @JsonProperty("articleTitle")
    private String articleTitle;//文章标题
    @JsonProperty("articleContent")
    private String articleContent;//文章内容
    @JsonProperty("isTop")
    private byte isTop;//是否置顶
    @JsonProperty("isDraft")
    private byte isDraft;//是否为草稿
    @JsonProperty("isDelete")
    private byte isDelete;//是否被删除

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public int getUserId() {
        return userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getArticleCover() {
        return articleCover;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticleContent() {
        return articleContent;
    }

    public byte getIsTop() {
        return isTop;
    }

    public byte getIsDraft() {
        return isDraft;
    }

    public byte getIsDelete() {
        return isDelete;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setArticleCover(String articleCover) {
        this.articleCover = articleCover;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public void setArticleContent(String articleContent) {
        this.articleContent = articleContent;
    }

    public void setIsTop(byte isTop) {
        this.isTop = isTop;
    }

    public void setIsDraft(byte isDraft) {
        this.isDraft = isDraft;
    }

    public void setIsDelete(byte isDelete) {
        this.isDelete = isDelete;
    }
}
