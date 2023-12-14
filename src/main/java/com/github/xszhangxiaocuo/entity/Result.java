package com.github.xszhangxiaocuo.entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

public class Result<T> {
    private int code;
    private T data;
    private String message;

    public void success(T data){
        this.code=0;
        this.data=data;
        this.message="OK";
    }

    public void failure(int code,String message){
        this.code=code;
        this.data=null;
        this.message = message;
    }
    //将当前result对象变为json格式的数据
    //JSONWriter.Feature.WriteNulls 是一个配置项，指示序列化过程应该包含值为 null 的字段
    public String asJsonString(){
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
