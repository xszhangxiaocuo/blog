package com.github.xszhangxiaocuo.servlet;

import com.github.xszhangxiaocuo.dao.UserInfoDao;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.sql.UserInfo;
import com.github.xszhangxiaocuo.utils.JsonUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "Silence", value = "/silence")
public class Silence extends HttpServlet {
    int CODE;
    int userId=0;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Result result = new Result();
        userId = 0;
        try {
            String param = request.getParameter("userId");
            if (param==null||param.isEmpty()){
                CODE = ErrCode.ERROR_USER_NOT_EXIST.getCode();
                result.failure(CODE, ErrMessage.getMsg(CODE));
            }else {
                userId = Integer.parseInt(param);
                UserInfo userInfo = UserInfoDao.query(userId);
                if (userInfo==null){
                    CODE = ErrCode.ERROR_USER_NOT_EXIST.getCode();
                    result.failure(CODE, ErrMessage.getMsg(CODE));
                }else {
                    result.success(userInfo.getIsSilence());
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JsonUtil.returnJSON(response,JsonUtil.beanToJson(result));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}