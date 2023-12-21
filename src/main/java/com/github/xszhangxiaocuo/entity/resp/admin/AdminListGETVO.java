package com.github.xszhangxiaocuo.entity.resp.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.xszhangxiaocuo.entity.sql.Article;

import java.util.List;

public class AdminListGETVO<T> {
    @JsonProperty("count")
    private int count;

    @JsonProperty("recordList")
    private List<T> recordList;

    public int getCount() {
        return count;
    }

    public List<T> getRecordList() {
        return recordList;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setRecordList(List<T> recordList) {
        this.recordList = recordList;
    }
}
