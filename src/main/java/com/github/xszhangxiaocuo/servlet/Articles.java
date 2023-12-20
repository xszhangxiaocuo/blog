package com.github.xszhangxiaocuo.servlet;

import com.github.xszhangxiaocuo.dao.ArticleDao;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.resp.ArticleVO;
import com.github.xszhangxiaocuo.entity.sql.Article;
import com.github.xszhangxiaocuo.utils.JsonUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Articles", value = "/articles/*")
public class Articles extends HttpServlet {
    int CODE;//业务代码
    int CURRENT=0;//当前页数
    int pageSize=5;//每一页五篇文章
    int articleId=0;//文章id
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // 构建响应数据对象
        Result result = new Result<>();
        List<Article> articles;
        try {
            String pathInfo = request.getPathInfo(); // 获取 URL 中的路径部分
            if (pathInfo != null) {//不为空表示获取特定文章信息
                pathInfo = pathInfo.replaceFirst("^/", "");//删除第一个“/”
                articleId= Integer.parseInt(pathInfo);
                articles = ArticleDao.query(articleId,ArticleDao.FINDBYARTID);
                if (articles.size()==0){
                    //文章不存在
                    CODE= ErrCode.ERROR_ART_NOT_EXIST.getCode();
                    result.failure(CODE, ErrMessage.getMsg(CODE));
                }else {
                    result.success(articles.get(0));
                }
            }else {//获取所有文章
                CURRENT = Integer.parseInt(request.getParameter("current"));//获取当前页数
                //还没做jwt解析验证用户，先查询全部
                articles = ArticleDao.query(0,ArticleDao.FINDALL,CURRENT,pageSize);
                if (articles.size()==0){
                    //文章列表为空
                    CODE= ErrCode.ERROR_ART_IS_NULL.getCode();
                    result.failure(CODE, ErrMessage.getMsg(CODE));
                }else {
                    ArticleVO articleVO = new ArticleVO();
                    articleVO.setArticleList(articles);
                    articleVO.setLength(articles.size());
                    result.success(articleVO);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JsonUtil.returnJSON(response, JsonUtil.beanToJson(result));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}