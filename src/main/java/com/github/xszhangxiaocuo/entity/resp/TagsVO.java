package com.github.xszhangxiaocuo.entity.resp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TagsVO {
    @JsonProperty("id")
    private int id;

    @JsonProperty("tagName")
    private String tagName;//标签名

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
