package com.github.xszhangxiaocuo.entity.req.admin;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdminArticlesReq {
    @JsonProperty("current")
    private int current;

    @JsonProperty("size")
    private int size;

    @JsonProperty("keywords")
    private String keywords;

    @JsonProperty("isDelete")
    private byte isDelete;

    @JsonProperty("isDraft")
    private byte isDraft;

    public int getCurrent() {
        return current;
    }

    public int getSize() {
        return size;
    }

    public String getKeywords() {
        return keywords;
    }

    public byte getIsDelete() {
        return isDelete;
    }

    public byte getIsDraft() {
        return isDraft;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setIsDelete(byte isDelete) {
        this.isDelete = isDelete;
    }

    public void setIsDraft(byte isDraft) {
        this.isDraft = isDraft;
    }
}
