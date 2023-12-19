package com.github.xszhangxiaocuo.servlet.admin;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.xszhangxiaocuo.dao.UserAuthDao;
import com.github.xszhangxiaocuo.dao.UserInfoDao;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.Token;
import com.github.xszhangxiaocuo.entity.req.LoginReq;
import com.github.xszhangxiaocuo.entity.resp.LoginVO;
import com.github.xszhangxiaocuo.entity.sql.UserAuth;
import com.github.xszhangxiaocuo.entity.sql.UserInfo;
import com.github.xszhangxiaocuo.utils.JsonUtil;
import com.github.xszhangxiaocuo.utils.JwtUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "adminlogin", value = "/admin/login")
public class AdminLogin extends HttpServlet {
    //token过期时间
    long DURATION = 6 * 60 * 60;
    //业务状态码
    int CODE =0;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String authorization = request.getHeader("Authorization");
//        System.out.println("Authorization:"+authorization);
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
            // 将json绑定到LoginReq对象
            LoginReq loginReq = JSON.toJavaObject(json, LoginReq.class);
            username = loginReq.getUsername();
            password = loginReq.getPassword();
        }

        UserAuth userAuth = UserAuthDao.query(username);

        if (username.isEmpty()||password.isEmpty()){
            //账号密码为空
            CODE =ErrCode.ERROR_VOID_ACCOUNT_PASSWORD.getCode();
            result.failure(CODE, ErrMessage.getMsg(CODE));
        }else if (userAuth==null){
            //用户不存在
            CODE =ErrCode.ERROR_USER_NOT_EXIST.getCode();
            result.failure(CODE,ErrMessage.getMsg(CODE));
        } else if (userAuth.getPassword().equals(password)==false) {
            //密码错误
            CODE =ErrCode.ERROR_PASSWORD_WRONG.getCode();
            result.failure(CODE,ErrMessage.getMsg(CODE));
        }else {
                UserInfo userInfo = UserInfoDao.query(userAuth.getUserInfoId());
                Map<String, String> param = new HashMap<>();
                String ipAddress = request.getRemoteAddr(); //获取IP
                String userAgent = request.getHeader("User-Agent");// 获取用户的浏览器信息
                param.put("userInfoId", String.valueOf(userInfo.getId()));
                param.put("username", username);
                param.put("ipAddress",ipAddress);
                param.put("userAgent",userAgent);
                // 如果用户名和密码验证成功，创建一个包含令牌的响应数据
                Token token = JwtUtil.generateJwt(param, DURATION); // 生成令牌
                LoginVO loginVO = new LoginVO();
                loginVO.setToken(token.getToken());
                loginVO.setId(userAuth.getId());
                loginVO.setUserInfoId(userAuth.getUserInfoId());
                loginVO.setNickname(userAuth.getUsername());
                loginVO.setAvatar(userInfo.getAvatar());
                loginVO.setIntro(userInfo.getIntro());
                loginVO.setWebSite(userInfo.getWebsite());
                loginVO.setUserRole(userInfo.getUserRole());

                result.success(loginVO);
                //token放入session
                HttpSession session = request.getSession();
                session.setAttribute("token",token);
            }
        //将json数据写入response
        JsonUtil.returnJSON(response,(JSONObject) JsonUtil.beanToJson(result));

    }
}