package com.github.xszhangxiaocuo.servlet.admin;

import com.github.xszhangxiaocuo.dao.ArticleDao;
import com.github.xszhangxiaocuo.dao.CommentDao;
import com.github.xszhangxiaocuo.dao.UserInfoDao;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.resp.admin.AdminGETVO;
import com.github.xszhangxiaocuo.utils.JsonUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "Admin", value = "/admin/home")
public class Admin extends HttpServlet {
    int CODE;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Result result = new Result();

        AdminGETVO getvo = new AdminGETVO();

        try {
            getvo.setMessageCount(CommentDao.query(0, CommentDao.FINDALL, (byte) 0).size());
            getvo.setArticleCount(ArticleDao.query(0, ArticleDao.FINDALL, (byte) 0, (byte) 0).size());
            getvo.setUserCount(UserInfoDao.query().size());
            result.success(getvo);
        }catch (Exception e){
            e.printStackTrace();
            CODE = ErrCode.FAIL.getCode();
            result.failure(CODE, ErrMessage.getMsg(CODE));
        }finally {
            JsonUtil.returnJSON(response,JsonUtil.beanToJson(result));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}