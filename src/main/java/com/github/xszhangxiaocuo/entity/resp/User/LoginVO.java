package entity.resp.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Date;

public class LoginVO {
    @JsonProperty("id")
    private int id;

    @JsonProperty("user_info_id")
    private int userInfoId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("intro")
    private String intro;

    @JsonProperty("website")
    private String website;

    @JsonProperty("ip_address")
    private String ipAddress;

    @JsonProperty("ip_source")
    private String ipSource;

    @JsonProperty("last_login_time")
    private Date lastLoginTime;

    @JsonProperty("login_type")
    private int loginType;

    @JsonProperty("article_like_set")
    private List<String> articleLikeSet;// 点赞 Set: 用于记录用户点赞过的文章

    @JsonProperty("comment_like_set")// 点赞 Set: 用于记录用户点赞过的评论
    private List<String> commentLikeSet;

    @JsonProperty("token")
    private String token;


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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpSource() {
        return ipSource;
    }

    public void setIpSource(String ipSource) {
        this.ipSource = ipSource;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
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
}

