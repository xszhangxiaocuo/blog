package com.github.xszhangxiaocuo.servlet.admin;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.xszhangxiaocuo.dao.ArticleDao;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.req.admin.AdminArticlesPOSTReq;
import com.github.xszhangxiaocuo.entity.resp.admin.AdminListGETVO;
import com.github.xszhangxiaocuo.entity.sql.Article;
import com.github.xszhangxiaocuo.utils.JsonUtil;
import com.github.xszhangxiaocuo.utils.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet(name = "adminUsers", value = "/admin/users/*")
public class AdminUsers extends HttpServlet {
    int CODE;//业务代码
    int CURRENT=0;//当前页数
    int pageSize=5;//默认每一页大小为5
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

                //查询全部
                articles = ArticleDao.query(0,ArticleDao.FINDALL,CURRENT,pageSize);
                if (articles==null||articles.size()==0){
                    //文章列表为空
                    CODE= ErrCode.ERROR_ART_IS_NULL.getCode();
                    result.failure(CODE, ErrMessage.getMsg(CODE));
                }else {
                    AdminListGETVO articleVO = new AdminListGETVO();
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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 获取request中对数据转换为JSON对象
        JSONObject json = JsonUtil.parseToJson(request);
        // 构建响应数据对象
        Result result = new Result<>();

        if (json!=null) {
            // 使用JSON对象中的数据
            // 将json绑定到AdminArticlesPOSTReq对象
            AdminArticlesPOSTReq postReq = JSON.toJavaObject(json, AdminArticlesPOSTReq.class);
            List<Article> articles = ArticleDao.query(postReq.getId(),ArticleDao.FINDBYARTID);
            Timestamp now = TimeUtil.getTimeStamp();
            if (articles==null) {//文章不存在就插入数据
                Article article = new Article(now);

                article.setUserId(postReq.getUserId());
                article.setArticleCover(postReq.getArticleCover());
                article.setArticleTitle(postReq.getArticleTitle());
                article.setArticleContent(postReq.getArticleContent());
                article.setUpdateTime(now);
                article.setIsTop(postReq.getIsTop());
                article.setIsDraft(postReq.getIsDraft());
                article.setIsDelete((byte) 0);

                CODE = ArticleDao.insert(article).getCode();
                if (CODE == ErrCode.OK.getCode()) {
                    result.success();
                } else {
                    result.failure(CODE, ErrMessage.getMsg(CODE));
                }
            }else {//文章已经存在就更新数据
                Article article = articles.get(0);
                article.setUserId(postReq.getUserId());
                if (postReq.getArticleCover()==null||postReq.getArticleCover().isEmpty()){
                    article.setArticleCover("https://github.com/xszhangxiaocuo/picBed/blob/master/picBed/lycoris.png?raw=true");
                }else {
                    article.setArticleCover(postReq.getArticleCover());
                }
                article.setArticleTitle(postReq.getArticleTitle());
                article.setArticleContent(postReq.getArticleContent());
                article.setUpdateTime(now);
                article.setIsTop(postReq.getIsTop());
                article.setIsDraft(postReq.getIsDraft());
                article.setIsDelete(postReq.getIsDelete());

                ArticleDao.update(article);

                result.success();
            }
        }
        JsonUtil.returnJSON(response,JsonUtil.beanToJson(result));
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