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
    private int articleCount;

    @JsonProperty("categoryCount")
    private int categoryCount;

    @JsonProperty("tagCount")
    private int tagCount;

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


    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public int getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(int articleCount) {
        this.articleCount = articleCount;
    }

    public int getCategoryCount() {
        return categoryCount;
    }

    public void setCategoryCount(int categoryCount) {
        this.categoryCount = categoryCount;
    }

    public int getTagCount() {
        return tagCount;
    }

    public void setTagCount(int tagCount) {
        this.tagCount = tagCount;
    }
}
