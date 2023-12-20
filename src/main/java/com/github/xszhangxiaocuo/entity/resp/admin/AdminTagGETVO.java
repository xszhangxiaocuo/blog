package com.github.xszhangxiaocuo.entity.resp.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.xszhangxiaocuo.entity.sql.Category;
import com.github.xszhangxiaocuo.entity.sql.Tag;

import java.util.List;

public class AdminTagGETVO {
    @JsonProperty("count")
    private int count;

    @JsonProperty("recordList")
    private List<Tag> recordList;

    public int getCount() {
        return count;
    }

    public List<Tag> getRecordList() {
        return recordList;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setRecordList(List<Tag> recordList) {
        this.recordList = recordList;
    }
}
