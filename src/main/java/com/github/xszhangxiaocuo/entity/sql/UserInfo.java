package com.github.xszhangxiaocuo.entity.sql;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

/**
 * 用户个人信息，user_info表
 */
public class UserInfo extends Universal{
    public static String userRoleName = "user_role";
    public static String nicknameName = "nickname";
    public static String avatarName = "avatar";
    public static String introName = "intro";
    public static String websiteName = "web_site";
    public static String isSilenceName = "is_silence";
    public static String blogTitleName = "blog_title";

    @JsonProperty("userRole")//指定 JSON 中的字段名称
    private String userRole;//用户角色
    @JsonProperty("nickname")
    private String nickname;//昵称
    @JsonProperty("avatar")
    private String avatar;//头像url
    @JsonProperty("intro")
    private String intro;//个人简介
    @JsonProperty("webSite")
    private String website;//个人网站
    @JsonProperty("isSilence")
    private byte isSilence;//是否禁言(0-否 1-是)

    @JsonProperty("blogTitle")
    private String blogTitle;//博客标题

    public UserInfo(){}

    public UserInfo(Timestamp now){
        setCreateTime(now);
    }


    public String getUserRole() {
        return userRole;
    }

    public byte getIsSilence() {
        return isSilence;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getIntro() {
        return intro;
    }

    public String getWebsite() {
        return website;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
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
