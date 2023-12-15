package com.github.xszhangxiaocuo.entity.sql;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.sql.Date;

//所有表中都有的字段
public class Universal {
    public String idName = "id";
    public String createAtName = "created_at";
    public String updatedAtName = "updated_at";
    @JsonProperty("id")
    private long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.fff")
    @JsonProperty("created_at")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.fff")
    @JsonProperty("updated_at")
    private Date updatedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
