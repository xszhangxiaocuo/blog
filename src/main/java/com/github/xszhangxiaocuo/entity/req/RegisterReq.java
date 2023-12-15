package com.github.xszhangxiaocuo.entity.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class RegisterReq {
    @NotBlank(message = "用户名不能为空")//用于确保相应的字段不为空，并提供一个错误消息。
    @JsonProperty("username")//指定 JSON 中的字段名称
    private String username;

    @NotBlank(message = "密码不能为空")
    @JsonProperty("password")
    private String password;

    @JsonProperty("code")
    private String code;//邮箱验证码

    // 标准的 getter 和 setter 方法
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
