package com.github.xszhangxiaocuo.entity.resp.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.xszhangxiaocuo.entity.sql.UserInfo;

import java.sql.Timestamp;

public class AdminUsersVO extends UserInfo {
    @JsonProperty("email")
    String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("lastLoginTime")
    private Timestamp lastLoginTime;//上次登录时间

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
