package com.github.xszhangxiaocuo.entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;

import java.io.Serializable;

public class Result<T> implements Serializable {
    private boolean flag;
    private int code;
    private T data;
    private String message;

    public void success(){
        flag=true;
        this.code= ErrCode.OK.getCode();
        this.data=null;
        this.message= ErrMessage.getMsg(code);
    }
    public void success(T data){
        flag=true;
        this.code= ErrCode.OK.getCode();
        this.data=data;
        this.message= ErrMessage.getMsg(code);
    }

    public void success(T data,String message){
        flag=true;
        this.code= ErrCode.OK.getCode();
        this.data=data;
        this.message= message;
    }

    public void failure(int code,String message){
        flag=false;
        this.code=code;
        this.data=null;
        this.message = message;
    }
    //将当前result对象变为json格式的数据
    //JSONWriter.Feature.WriteNulls 是一个配置项，指示序列化过程应该包含值为 null 的字段
    public String asJsonString(){
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
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
