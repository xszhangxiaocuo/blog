package com.github.xszhangxiaocuo.entity.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.xszhangxiaocuo.entity.sql.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleVO {
    @JsonProperty("length")
    private int length;

    @JsonProperty("articleList")
    private List<Article> articleList;

    public int getLength() {
        return length;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

}
