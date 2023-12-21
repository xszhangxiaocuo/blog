package com.github.xszhangxiaocuo.entity.sql;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArticleTag {
    public static String idName = "id";
    public static String articleIdName = "article_id";
    public static String tagIdName = "tag_id";

    @JsonProperty("id")
    private int id;

    @JsonProperty("articleId")
    private int articleId;//文章id
    @JsonProperty("tagId")
    private int tagId;//分类id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }
}
