package com.github.xszhangxiaocuo.entity.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class BlogInfoReq {
    @NotBlank(message = "用户名不能为空")//用于确保相应的字段不为空，并提供一个错误消息。
    @JsonProperty("nickname")//指定 JSON 中的字段名称
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
