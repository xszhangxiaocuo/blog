package com.github.xszhangxiaocuo.entity.sql;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class Category extends Universal{
    public static String categoryNameName = "category_name";
    public static String userIdName = "user_id";

    @JsonProperty("categoryName")
    private String categoryName;//文章分类

    @JsonProperty("userId")
    private int userId;//用户id

    public Category(){}

    public Category(Timestamp now){
        setCreateTime(now);
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getUserId() {
        return userId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
