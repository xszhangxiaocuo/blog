package com.github.xszhangxiaocuo.entity.sql;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class Tag extends Universal{
    public static String tagNameName = "tag_name";
    public static String userIdName = "user_id";

    @JsonProperty("tagName")
    private String tagName;//文章分类

    @JsonProperty("userId")
    private int userId;//用户id

    public Tag(){}

    public Tag(Timestamp now){
        setCreateTime(now);
    }

    public String getTagName() {
        return tagName;
    }

    public int getUserId() {
        return userId;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
