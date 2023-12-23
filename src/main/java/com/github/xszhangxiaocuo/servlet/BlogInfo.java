package com.github.xszhangxiaocuo.servlet;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.xszhangxiaocuo.dao.ArticleDao;
import com.github.xszhangxiaocuo.dao.CategoryDao;
import com.github.xszhangxiaocuo.dao.TagDao;
import com.github.xszhangxiaocuo.dao.UserInfoDao;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.req.BlogInfoReq;
import com.github.xszhangxiaocuo.entity.resp.BlogInfoVO;
import com.github.xszhangxiaocuo.entity.sql.UserInfo;
import com.github.xszhangxiaocuo.utils.JsonUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "BlogInfo", value = "/bloginfo")
public class BlogInfo extends HttpServlet {
    int CODE;
    byte isDelete=0;//默认查询未删除文章
    byte isDraft=0;//默认查询已发布文章
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int userId = 0;
        // 构建响应数据对象
        Result result = new Result<>();

            String blogInfoReq = request.getParameter("userId");
            if (blogInfoReq!=null&&!blogInfoReq.isEmpty()){
                userId = Integer.parseInt(blogInfoReq);
            }
        UserInfo userInfo = UserInfoDao.query(userId);
        if (userInfo==null){
            //用户不存在
            CODE = ErrCode.ERROR_USER_NOT_EXIST.getCode();
            result.failure(CODE, ErrMessage.getMsg(CODE));
        }else {
            BlogInfoVO blogInfoVO = new BlogInfoVO();

            blogInfoVO.setId(userInfo.getId());
            blogInfoVO.setNickname(userInfo.getNickname());
            blogInfoVO.setAvatar(userInfo.getAvatar());
            blogInfoVO.setIntro(userInfo.getIntro());
            blogInfoVO.setArticleCount(ArticleDao.query(userInfo.getId(),ArticleDao.FINDBYUSERID,isDelete,isDraft).size());
            blogInfoVO.setTagCount(TagDao.query(userInfo.getId(),TagDao.FINDBYUSERID).size());
            blogInfoVO.setCategoryCount(CategoryDao.query(userInfo.getId(),CategoryDao.FINDBYUSERID).size());
            blogInfoVO.setBlogTitle(userInfo.getBlogTitle());

            result.success(blogInfoVO);
        }
        JsonUtil.returnJSON(response, JsonUtil.beanToJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}