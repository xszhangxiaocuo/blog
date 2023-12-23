package com.github.xszhangxiaocuo.entity.resp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListVO<T> {
    @JsonProperty("count")
    private int count;

    @JsonProperty("recordList")
    private List<T> recordList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<T> recordList) {
        this.recordList = recordList;
    }
}
