package com.github.xszhangxiaocuo.entity.resp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BlogInfoVO {
    @JsonProperty("id")
    private int id;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("intro")
    private String intro;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("articleCount")
    private String articleCount;

    @JsonProperty("categoryCount")
    private String categoryCount;

    @JsonProperty("tagCount")
    private String tagCount;

    @JsonProperty("blogTitle")
    private String blogTitle;//博客标题

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getIntro() {
        return intro;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getArticleCount() {
        return articleCount;
    }

    public String getCategoryCount() {
        return categoryCount;
    }

    public String getTagCount() {
        return tagCount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setArticleCount(String articleCount) {
        this.articleCount = articleCount;
    }

    public void setCategoryCount(String categoryCount) {
        this.categoryCount = categoryCount;
    }

    public void setTagCount(String tagCount) {
        this.tagCount = tagCount;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }
}
