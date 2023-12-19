package com.github.xszhangxiaocuo.entity.sql;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

//所有表中都有的字段
public class Universal {
    public static String idName = "id";
    public static String createTimeName = "create_time";

    @JsonProperty("id")
    private int id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonProperty("create_time")
    private Timestamp createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

}
