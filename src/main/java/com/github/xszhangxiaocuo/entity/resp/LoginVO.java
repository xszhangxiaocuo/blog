package com.github.xszhangxiaocuo.entity.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Date;

public class LoginVO {
    @JsonProperty("id")
    private int id;

    @JsonProperty("userInfoId")
    private int userInfoId;

    @JsonProperty("userRole")
    private String userRole;

    @JsonProperty("username")
    private String username;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("intro")
    private String intro;

    @JsonProperty("webSite")
    private String webSite;

    @JsonProperty("isSilence")
    private byte isSilence;//禁言状态

    @JsonProperty("blogTitle")
    private String blogTitle;//禁言状态

    @JsonProperty("articleLikeSet")
    private List<String> articleLikeSet;// 点赞 Set: 用于记录用户点赞过的文章

    @JsonProperty("commentLikeSet")// 点赞 Set: 用于记录用户点赞过的评论
    private List<String> commentLikeSet;

    @JsonProperty("token")
    private String token;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(int userInfoId) {
        this.userInfoId = userInfoId;
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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public List<String> getArticleLikeSet() {
        return articleLikeSet;
    }

    public void setArticleLikeSet(List<String> articleLikeSet) {
        this.articleLikeSet = articleLikeSet;
    }

    public List<String> getCommentLikeSet() {
        return commentLikeSet;
    }

    public void setCommentLikeSet(List<String> commentLikeSet) {
        this.commentLikeSet = commentLikeSet;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public byte getIsSilence() {
        return isSilence;
    }

    public void setIsSilence(byte isSilence) {
        this.isSilence = isSilence;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }
}

