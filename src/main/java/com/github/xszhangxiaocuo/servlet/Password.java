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
import com.github.xszhangxiaocuo.utils.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;

@WebServlet(name = "Password", value = "/users/password")
public class Password extends HttpServlet {
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
            if (exist==null){
                //用户不存在
                CODE=ErrCode.ERROR_USER_NOT_EXIST.getCode();
                result.failure(CODE, ErrMessage.getMsg(CODE));
            }else {
                //更新密码
                UserAuth userAuth = UserAuthDao.query(username);
                if (userAuth!=null){
                    userAuth.setPassword(password);
                    CODE = UserAuthDao.update(userAuth).getCode();
                }
                if (CODE==ErrCode.OK.getCode()){
                    result.success();
                }else {
                    result.failure(CODE,ErrMessage.getMsg(CODE));
                }
            }
        }else {//验证失败
            CODE = ErrCode.ERROR_VERIFICATION_CODE.getCode();
            result.failure(CODE,ErrMessage.getMsg(CODE));
        }
        JsonUtil.returnJSON(response,JsonUtil.beanToJson(result));
    }
}