package com.github.xszhangxiaocuo.entity.resp.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.xszhangxiaocuo.entity.sql.Article;

import java.util.List;

public class AdminArticleGETVO {
    @JsonProperty("count")
    private int count;

    @JsonProperty("recordList")
    private List<Article> recordList;

    public int getCount() {
        return count;
    }

    public List<Article> getRecordList() {
        return recordList;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setRecordList(List<Article> recordList) {
        this.recordList = recordList;
    }
}
