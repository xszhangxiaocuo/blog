package com.github.xszhangxiaocuo.entity.req.admin;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AdminCategoriesGETReq {
    @JsonProperty("current")
    private int current;

    @JsonProperty("size")
    private int size;

    @JsonProperty("keywords")
    private String keywords;

    public int getCurrent() {
        return current;
    }

    public int getSize() {
        return size;
    }

    public String getKeywords() {
        return keywords;
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

}
