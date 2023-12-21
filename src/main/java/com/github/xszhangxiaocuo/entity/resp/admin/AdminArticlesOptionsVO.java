package com.github.xszhangxiaocuo.entity.resp.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.xszhangxiaocuo.entity.sql.Category;
import com.github.xszhangxiaocuo.entity.sql.Tag;

import java.util.List;

public class AdminArticlesOptionsVO {

    @JsonProperty("tagList")
    private List<Tag> tagList;

    @JsonProperty("categoryList")
    private List<Category> categoryList;

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
