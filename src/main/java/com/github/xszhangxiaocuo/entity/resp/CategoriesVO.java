package com.github.xszhangxiaocuo.entity.resp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoriesVO {
    @JsonProperty("id")
    private int id;

    @JsonProperty("articleCount")
    private int articleCount;//该分类有多少个文章

    @JsonProperty("categoryName")
    private String categoryName;//分类名

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
