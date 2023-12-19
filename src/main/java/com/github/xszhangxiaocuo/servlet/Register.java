package com.github.xszhangxiaocuo.servlet;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.xszhangxiaocuo.dao.UserAuthDao;
import com.github.xszhangxiaocuo.dao.UserInfoDao;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.req.RegisterReq;
import com.github.xszhangxiaocuo.entity.sql.UserAuth;
import com.github.xszhangxiaocuo.entity.sql.UserInfo;
import com.github.xszhangxiaocuo.utils.JsonUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebServlet(name = "register", value = "/users/register")
public class Register extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String username = "",password="",code="";
        // 获取request中对数据转换为JSON对象
        JSONObject json = JsonUtil.parseToJson(request);
        // 构建响应数据对象
        Result result = new Result<>();
        //业务状态码
        int CODE=0;

        if (json!=null) {
            // 使用JSON对象中的数据
            // 将json绑定到RegisterReq对象
            RegisterReq registerReq = JSON.toJavaObject(json, RegisterReq.class);
            username = registerReq.getUsername();
            password = registerReq.getPassword();
            code = registerReq.getCode();
        }

        if (username.isEmpty()||password.isEmpty()){
            //账号密码为空
            CODE= ErrCode.ERROR_VOID_ACCOUNT_PASSWORD.getCode();
            result.failure(CODE, ErrMessage.getMsg(CODE));
        }else if(code.equals(Code.CAPTCHA)){//验证码正确
            UserInfo exist = UserInfoDao.query(username);
            if (exist!=null){
                //用户已存在
                CODE=ErrCode.ERROR_USER_NAME_USED.getCode();
                result.failure(CODE, ErrMessage.getMsg(CODE));
            }else {
                //获取当前时间
                LocalDateTime now = LocalDateTime.now();
                Timestamp timestamp = Timestamp.valueOf(now);
                UserInfo userInfo = new UserInfo(timestamp);
                //用户角色默认为普通用户
                userInfo.setUserRole("普通用户");
                //昵称默认为邮箱
                userInfo.setNickname(username);
                //注册使用默认头像
                userInfo.setAvatar("https://github.com/xszhangxiaocuo/picBed/blob/master/picBed/headerimage.jpg?raw=true");
                //默认不禁言
                userInfo.setIsSilence((byte)0);
                //博客标题默认为nickname的博客
                userInfo.setBlogTitle(username+"的博客");
                //新建用户
                UserInfoDao.insert(userInfo);

                int id = UserInfoDao.query(username).getId();//获取刚刚插入的userInfo的id作为user_info_id

                UserAuth userAuth = new UserAuth(timestamp);
                userAuth.setUserInfoId(id);
                userAuth.setUsername(username);
                userAuth.setPassword(password);
                userAuth.setLoginType((byte)1);
                userAuth.setLastLoginTime(timestamp);
                UserAuthDao.insert(userAuth);

                //注册完成
                result.success();
            }
        }else {//验证失败
            CODE = ErrCode.ERROR_VERIFICATION_CODE.getCode();
            result.failure(CODE,ErrMessage.getMsg(CODE));
        }
        JsonUtil.returnJSON(response,JsonUtil.beanToJson(result));
    }
}