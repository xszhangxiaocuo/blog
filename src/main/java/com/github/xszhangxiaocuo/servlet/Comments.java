package com.github.xszhangxiaocuo.servlet;

import com.github.xszhangxiaocuo.dao.ArticleDao;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.resp.ArticleVO;
import com.github.xszhangxiaocuo.entity.sql.Article;
import com.github.xszhangxiaocuo.utils.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Comments", value = "/comments")
public class Comments extends HttpServlet {
    int CODE;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
//        int userId = 0;
//        // 获取request中对数据转换为JSON对象
//        //JSONObject json = JsonUtil.parseToJson(request);
//        // 构建响应数据对象
//        Result result = new Result<>();
//        List<Article> articles = ArticleDao.query(21);
//        ArticleVO articleVO = new ArticleVO();
//        articleVO.setArticleList(articles);
//        articleVO.setLength(1);
//        result.success(articleVO);
//        JsonUtil.returnJSON(response, JsonUtil.beanToJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}