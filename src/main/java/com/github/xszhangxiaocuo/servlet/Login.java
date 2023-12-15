package com.github.xszhangxiaocuo.servlet;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.xszhangxiaocuo.dao.UserAuthDao;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.Token;
import com.github.xszhangxiaocuo.entity.sql.UserAuth;
import com.github.xszhangxiaocuo.utils.JsonUtil;
import com.github.xszhangxiaocuo.utils.JwtUtil;
import com.github.xszhangxiaocuo.entity.req.LoginReq;
import entity.resp.User.LoginVO;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "login", value = "/login")
public class Login extends HttpServlet {
    long DURATION = 6 * 60 * 60;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        System.out.println("Authorization:"+authorization);
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
        //获取user_auth表的连接
        UserAuthDao db = new UserAuthDao();
        //业务状态码
        int code=0;

        if (json!=null) {
            // 使用JSON对象中的数据
            // 将json绑定到User
            LoginReq loginVO = JSON.toJavaObject(json, LoginReq.class);
            username = loginVO.getUsername();
            password = loginVO.getPassword();
        }

        UserAuth userAuth = db.query(username);

        if (username.isEmpty()||password.isEmpty()){
            //账号密码为空
            code=ErrCode.ERROR_VOID_ACCOUNT_PASSWORD.getCode();
            result.failure(code, ErrMessage.getMsg(code));
        }else if (userAuth==null){
            //用户不存在
            code=ErrCode.ERROR_USER_NOT_EXIST.getCode();
            result.failure(code,ErrMessage.getMsg(code));
        } else if (userAuth.getPassword().equals(password)==false) {
            //密码错误
            code=ErrCode.ERROR_PASSWORD_WRONG.getCode();
            result.failure(code,ErrMessage.getMsg(code));
        }else {
                Map<String, String> param = new HashMap<>();
                String ipAddress = request.getRemoteAddr(); //获取IP
                String userAgent = request.getHeader("User-Agent");// 获取用户的浏览器信息
                param.put("userId", String.valueOf(userAuth.getId()));
                param.put("username", username);
                param.put("userAgent",userAgent);
                // 如果用户名和密码验证成功，创建一个包含令牌的响应数据
                Token token = JwtUtil.generateJwt(param, DURATION); // 生成令牌
                LoginVO loginVO = new LoginVO();
                loginVO.setToken(token.getToken());
                result.success(loginVO);
                //token放入session
                HttpSession session = request.getSession();
                session.setAttribute("token",token);
            }
        //将json数据写入response
        JsonUtil.returnJSON(response,JsonUtil.beanToJson(result));

    }
}