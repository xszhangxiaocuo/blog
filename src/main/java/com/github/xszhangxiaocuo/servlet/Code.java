package com.github.xszhangxiaocuo.servlet;

import com.alibaba.fastjson2.JSONObject;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.utils.CaptchaUtil;
import com.github.xszhangxiaocuo.utils.JsonUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Random;

@WebServlet(name = "Code", value = "/users/code")
public class Code extends HttpServlet {
    static String CAPTCHA;//验证码
    int CODE;//业务代码

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Result result = new Result();

        String username = "";
        username = request.getParameter("username");
        if (username.isEmpty()){
            //用户名为空
            CODE = ErrCode.ERROR_VOID_ACCOUNT_PASSWORD.getCode();
            result.failure(CODE, ErrMessage.getMsg(CODE));
        }else {
            CAPTCHA = CaptchaUtil.generateCaptcha(6);
            result.success(CAPTCHA);
        }
        JsonUtil.returnJSON(response, (JSONObject) JsonUtil.beanToJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}