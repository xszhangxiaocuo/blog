package com.github.xszhangxiaocuo.entity.resp.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.xszhangxiaocuo.entity.sql.Article;
import com.github.xszhangxiaocuo.entity.sql.Category;

import java.util.List;

public class AdminCategoriesGETVO {
    @JsonProperty("count")
    private int count;

    @JsonProperty("recordList")
    private List<Category> recordList;

    public int getCount() {
        return count;
    }

    public List<Category> getRecordList() {
        return recordList;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setRecordList(List<Category> recordList) {
        this.recordList = recordList;
    }
}
