package com.github.xszhangxiaocuo.entity.req.admin;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AdminTagDELETEReq {
    @JsonProperty("data")
    private int[] data;

    public int[] getData() {
        return data;
    }

    public void setData(int[] data) {
        this.data = data;
    }
}
