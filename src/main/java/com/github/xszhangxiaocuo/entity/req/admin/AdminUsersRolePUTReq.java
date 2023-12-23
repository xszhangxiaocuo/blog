package com.github.xszhangxiaocuo.entity.req.admin;


import com.fasterxml.jackson.annotation.JsonProperty;

public class AdminUsersRolePUTReq {
    @JsonProperty("id")
    private int id;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("userRole")
    private String userRole;

    @JsonProperty("isSilence")
    private byte isSilence;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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
}
