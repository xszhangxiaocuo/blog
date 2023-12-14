package com.github.xszhangxiaocuo.servlet;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.Token;
import com.github.xszhangxiaocuo.entity.req.User;
import com.github.xszhangxiaocuo.utils.JsonUtil;
import com.github.xszhangxiaocuo.utils.JwtUtil;
import entity.resp.User.LoginVO;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@WebServlet(name = "login", value = "/login")
public class Login extends HttpServlet {
    long DURATION = 6 * 60 * 60;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String username = "",password="";

        // 获取request中对数据转换为JSON对象
        JSONObject json = JsonUtil.parseToJson(request);
        // 构建响应数据对象
        Result result = new Result<>();
        if (json!=null) {
            // 使用JSON对象中的数据
            // 将json绑定到User
            User user = JSON.toJavaObject(json,User.class);
            username = user.getUsername();
            password = user.getPassword();
        }

        if (username.isEmpty()||password.isEmpty()){
            //返回登录失败
            result.failure(1,"账号或密码为空");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        }else if (username.equals("xszxc") && password.equals("123")) {
                Map<String, String> param = new HashMap<>();
                param.put("userId", "123455");
                param.put("username", username);
                // 如果用户名和密码验证成功，创建一个包含令牌的响应数据
                Token token = JwtUtil.generateJwt(param, DURATION); // 生成令牌
                //String userInfo = getUserInfo(username); // 获取用户信息
                LoginVO loginVO = new LoginVO();
                result.success(loginVO);
               // responseData.put("token", token.getToken());
                //responseData.put("userInfo", userInfo);
            response.setStatus(HttpServletResponse.SC_OK);
            }else {
            result.failure(2,"账号或密码错误!");
        }

        JsonUtil.returnJSON(response,JsonUtil.beanToJson(result));

    }
}