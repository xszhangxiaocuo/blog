package com.github.xszhangxiaocuo.entity.sql;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户账户信息，user_auth表
 */
public class UserAuth extends Universal{
    public static String userInfoIdName = "user_info_id";
    public static String usernameName = "username";
    public static String passwordName = "password";
    public static String loginTypeName = "login_type";
    public static String ipAddressName = "ip_addr";
    public static String ipSourceName = "ip_source";
    public static String lastLoginTimeName = "last_login_time";

    @JsonProperty("userInfoId")
    private int userInfoId;//用户信息ID

    @JsonProperty("username")
    private String username;//用户名

    @JsonProperty("password")
    private String password;//密码

    @JsonProperty("loginType")
    private byte loginType;//登录类型

    @JsonProperty("ipAddr")
    private String ipAddress;//登录IP地址

    @JsonProperty("ipSource")
    private String ipSource;//IP来源

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("lastLoginTime")
    private Timestamp lastLoginTime;//上次登录时间

    public UserAuth(){};

    public UserAuth(Timestamp now){
        setCreateTime(now);
    };

    public int getUserInfoId() {
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

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setUserInfoId(int userInfoId) {
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

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
