package com.github.xszhangxiaocuo.servlet.admin;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.xszhangxiaocuo.dao.ArticleDao;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.req.LoginReq;
import com.github.xszhangxiaocuo.entity.req.admin.AdminArticlesReq;
import com.github.xszhangxiaocuo.entity.resp.ArticleVO;
import com.github.xszhangxiaocuo.entity.resp.admin.AdminArticleVO;
import com.github.xszhangxiaocuo.entity.sql.Article;
import com.github.xszhangxiaocuo.utils.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "adminArticles", value = "/admin/articles/*")
public class AdminArticles extends HttpServlet {
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
            // 获取request中对数据转换为JSON对象
            JSONObject json = JsonUtil.parseToJson(request);

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
                CURRENT = Integer.parseInt(request.getParameter("current"));
                pageSize = Integer.parseInt(request.getParameter("size"));

                System.out.println("当前页面："+CURRENT);
                System.out.println("每页大小："+pageSize);
                //还没做jwt解析验证用户，先查询全部
                articles = ArticleDao.query(0,ArticleDao.FINDALL,CURRENT,pageSize);
                if (articles==null||articles.size()==0){
                    //文章列表为空
                    CODE= ErrCode.ERROR_ART_IS_NULL.getCode();
                    result.failure(CODE, ErrMessage.getMsg(CODE));
                }else {
                    AdminArticleVO articleVO = new AdminArticleVO();
                    articleVO.setRecordList(articles);
                    articleVO.setCount(articles.size());
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

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收文章id数组进行删除
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收文章id数组进行删除
    }
}