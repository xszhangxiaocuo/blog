package com.github.xszhangxiaocuo.servlet;

import com.alibaba.fastjson2.JSONObject;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.utils.JsonUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "Logout", value = "/logout")
public class Logout extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Result result = new Result();
        result.success();
        JsonUtil.returnJSON(response, JsonUtil.beanToJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}