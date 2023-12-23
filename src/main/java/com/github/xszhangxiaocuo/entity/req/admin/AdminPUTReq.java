package com.github.xszhangxiaocuo.entity.req.admin;


import com.fasterxml.jackson.annotation.JsonProperty;

public class AdminPUTReq {
    @JsonProperty("idList")
    private int[] idList;

    @JsonProperty("isDelete")
    private byte isDelete;

    @JsonProperty("isDraft")
    private byte isDraft;

    @JsonProperty("userId")
    private int userId;

    public int[] getIdList() {
        return idList;
    }

    public void setIdList(int[] idList) {
        this.idList = idList;
    }

    public byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(byte isDelete) {
        this.isDelete = isDelete;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public byte getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(byte isDraft) {
        this.isDraft = isDraft;
    }
}
