package com.github.xszhangxiaocuo.entity.sql;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

/**
 * 用户账户信息
 */
public class UserAuth extends Universal{
    public String userInfoIdName = "user_info_id";
    public String usernameName = "username";
    public String passwordName = "password";
    public String loginTypeName = "login_type";
    public String ipAddressName = "ip_address";
    public String ipSourceName = "ip_source";
    public String lastLoginTimeName = "last_login_time";

    private long userInfoId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("login_type")
    private byte loginType;

    @JsonProperty("ip_address")
    private String ipAddress;

    @JsonProperty("ip_source")
    private String ipSource;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.fff")
    @JsonProperty("last_login_time")
    private Date lastLoginTime;

    public long getUserInfoId() {
        return userInfoId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public byte getLoginType() {
        return loginType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getIpSource() {
        return ipSource;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setUserInfoId(long userInfoId) {
        this.userInfoId = userInfoId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLoginType(byte loginType) {
        this.loginType = loginType;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setIpSource(String ipSource) {
        this.ipSource = ipSource;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
