package com.github.xszhangxiaocuo.servlet;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String nickname = "";
        // 获取request中对数据转换为JSON对象
        JSONObject json = JsonUtil.parseToJson(request);
        // 构建响应数据对象
        Result result = new Result<>();

        if (json!=null) {
            // 使用JSON对象中的数据
            // 将json绑定到LoginReq对象
            BlogInfoReq blogInfoReq = JSON.toJavaObject(json, BlogInfoReq.class);
            nickname = blogInfoReq.getNickname();
            System.out.println("nickname："+nickname);
        }
        nickname = request.getParameter("nickname");
        UserInfo userInfo = UserInfoDao.query(nickname);
        if (userInfo==null){
            //用户不存在
            CODE = ErrCode.ERROR_USER_NOT_EXIST.getCode();
            result.failure(CODE, ErrMessage.getMsg(CODE));
        }else {
            BlogInfoVO blogInfoVO = new BlogInfoVO();

            blogInfoVO.setId(userInfo.getId());
            blogInfoVO.setNickname(nickname);
            blogInfoVO.setAvatar(userInfo.getAvatar());
            blogInfoVO.setIntro(userInfo.getIntro());
            //先不做统计
            blogInfoVO.setArticleCount("1");
            blogInfoVO.setTagCount("0");
            blogInfoVO.setCategoryCount("0");
            blogInfoVO.setBlogTitle(userInfo.getBlogTitle());

            result.success(blogInfoVO);
        }
        JsonUtil.returnJSON(response,(JSONObject) JsonUtil.beanToJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}