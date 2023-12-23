package com.github.xszhangxiaocuo.servlet.admin;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.xszhangxiaocuo.dao.*;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.req.admin.AdminPUTReq;
import com.github.xszhangxiaocuo.entity.req.admin.AdminArticlesPOSTReq;
import com.github.xszhangxiaocuo.entity.req.admin.AdminDELETEReq;
import com.github.xszhangxiaocuo.entity.resp.ListVO;
import com.github.xszhangxiaocuo.entity.resp.admin.AdminArticlesOptionsVO;
import com.github.xszhangxiaocuo.entity.resp.admin.AdminListGETVO;
import com.github.xszhangxiaocuo.entity.resp.admin.AdminUsersVO;
import com.github.xszhangxiaocuo.entity.sql.*;
import com.github.xszhangxiaocuo.utils.JsonUtil;
import com.github.xszhangxiaocuo.utils.PicBedutil;
import com.github.xszhangxiaocuo.utils.TimeUtil;
import io.jsonwebtoken.impl.lang.Bytes;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@WebServlet(name = "adminArticles", value = "/admin/articles/*")
@MultipartConfig//处理多部分请求
public class AdminArticles extends HttpServlet {
    int CODE;//业务代码
    int CURRENT=0;//当前页数
    int pageSize=5;//默认每一页大小为5
    int count=0;//总记录数
    byte isDelete=0;//默认查询未删除文章
    byte isDraft=0;//默认查询已发布文章
    int articleId=0;//文章id
    int userId = 0;//用户id
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        isDraft = 0;
        isDelete = 0;
        userId = 0;
        // 构建响应数据对象
        Result result = new Result<>();
        List<Article> articles = new ArrayList<>();
        try {
            String pathInfo = request.getPathInfo(); // 获取 URL 中的路径部分

            if (pathInfo != null&&!pathInfo.isEmpty()&&!pathInfo.startsWith("?")) {//不为空表示获取特定文章信息
                // 去除开头的斜杠（如果存在）
                if (pathInfo.startsWith("/")) {
                    pathInfo = pathInfo.substring(1);
                }

                // 使用“/”分割路径
                String[] pathParts = pathInfo.split("/");

                if (pathParts[0].equals("options")){
                    result = articlesOptions(request,response);
                }else if (pathParts[0].equals("images")){
                    result = articlesImages(request,response);
                }else {

                    articleId = Integer.parseInt(pathInfo);
                    articles = ArticleDao.query(articleId,ArticleDao.FINDBYARTID,isDelete,isDraft);
                    if (articles.size() == 0) {
                        //文章不存在
                        CODE = ErrCode.ERROR_ART_NOT_EXIST.getCode();
                        result.failure(CODE, ErrMessage.getMsg(CODE));
                    } else {
                        result.success(articles.get(0));
                    }
                }
            }else {//获取所有文章
                CURRENT = Integer.parseInt(request.getParameter("current"));
                pageSize = Integer.parseInt(request.getParameter("size"));
                if (request.getParameter("userId")!=null){
                    userId = Integer.parseInt(request.getParameter("userId"));
                }
                if (request.getParameter("isDelete")!=null){
                    isDelete = Byte.parseByte(request.getParameter("isDelete"));
                }
                if (request.getParameter("isDraft")!=null){
                    isDraft = Byte.parseByte(request.getParameter("isDraft"));
                }

                UserInfo userInfo = UserInfoDao.query(userId);
                if (userInfo==null){
                    CODE = ErrCode.ERROR_USER_NOT_EXIST.getCode();
                    result.failure(CODE,ErrMessage.getMsg(CODE));
                }else if (userInfo.getUserRole().equals("管理员")){
                    //管理员查询所有文章
                    articles = ArticleDao.query(0, ArticleDao.FINDALL, isDelete, isDraft, CURRENT, pageSize);
                    count = ArticleDao.query(0, ArticleDao.FINDALL, isDelete, isDraft).size();
                }else if (userInfo.getUserRole().equals("普通用户")){
                    //普通用户只能查询自己的文章
                    articles = ArticleDao.query(userId, ArticleDao.FINDBYUSERID,isDelete,isDraft,CURRENT,pageSize);
                    count = ArticleDao.query(userId,ArticleDao.FINDBYUSERID,isDelete,isDraft).size();
                }
                if (articles.isEmpty()) {
                    //文章列表为空
                    CODE = ErrCode.ERROR_ART_IS_NULL.getCode();
                    result.failure(CODE, ErrMessage.getMsg(CODE));
                } else {
                    AdminListGETVO articleVO = new AdminListGETVO();
                    articleVO.setRecordList(articles);
                    articleVO.setCount(count);
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

        // 构建响应数据对象
        Result result = new Result<>();

        String pathInfo = request.getPathInfo(); // 获取 URL 中的路径部分
        // 获取request中对数据转换为JSON对象
        //JSONObject json = JsonUtil.parseToJson(request);
        try {
            if (pathInfo != null && !pathInfo.isEmpty()) {//不为空表示获取特定文章信息
                // 去除开头的斜杠（如果存在）
                if (pathInfo.startsWith("/")) {
                    pathInfo = pathInfo.substring(1);
                }

                // 使用“/”分割路径
                String[] pathParts = pathInfo.split("/");

                if (pathParts[0].equals("images")) {
                    result = articlesImages(request, response);
                } else if (pathParts[0].equals("top")) {
                    articleId = Integer.parseInt(pathParts[1]);
                    result = articlesTop(request, response);
                }

            } else {

                // 获取request中对数据转换为JSON对象
                JSONObject json = JsonUtil.parseToJson(request);
                if (json != null) {
                    // 使用JSON对象中的数据
                    // 将json绑定到AdminArticlesPOSTReq对象
                    AdminArticlesPOSTReq postReq = JSON.toJavaObject(json, AdminArticlesPOSTReq.class);
                    List<Article> articles = ArticleDao.query(postReq.getId(), ArticleDao.FINDBYARTID,isDelete,isDraft);
                    Timestamp now = TimeUtil.getTimeStamp();
                    if (articles.isEmpty()) {//文章不存在就插入数据
                        Article article = new Article(now);

                        if (postReq.getArticleCover() == null || postReq.getArticleCover().isEmpty()) {
                            article.setArticleCover("https://github.com/xszhangxiaocuo/picBed/blob/master/picBed/lycoris.png?raw=true");
                        } else {
                            article.setArticleCover(postReq.getArticleCover());
                        }

                        article.setUserId(postReq.getUserId());
                        article.setArticleTitle(postReq.getArticleTitle());
                        article.setArticleContent(postReq.getArticleContent());
                        article.setUpdateTime(now);
                        article.setIsTop(postReq.getIsTop());
                        article.setIsDraft(postReq.getIsDraft());
                        article.setIsDelete((byte) 0);
                        article.setCategoryId(postReq.getCategoryId());

                        CODE = ArticleDao.insert(article).getCode();
                        List<Article> articleList = ArticleDao.query(0, ArticleDao.FINDALL,isDelete,isDraft);
                        if (!articleList.isEmpty()) {
                            //添加文章标签
                            List<Integer> tagList = postReq.getTagIdList();
                            if (tagList != null) {
                                for (Integer i : tagList) {
                                    ArticleTag at = new ArticleTag();
                                    at.setArticleId(articleList.get(articleList.size() - 1).getId());
                                    at.setTagId(i);
                                    CODE = ArticleTagDao.insert(at).getCode();
                                    if (CODE != ErrCode.OK.getCode()) {
                                        break;
                                    }
                                }
                            }
                        }

                        if (CODE == ErrCode.OK.getCode()) {
                            result.success();
                        } else {
                            result.failure(CODE, ErrMessage.getMsg(CODE));
                        }
                    } else {//文章已经存在就更新数据
                        Article article = articles.get(0);
                        article.setId(postReq.getId());
                        article.setUserId(postReq.getUserId());
                        if (postReq.getArticleCover() == null || postReq.getArticleCover().isEmpty()) {
                            article.setArticleCover("https://github.com/xszhangxiaocuo/picBed/blob/master/picBed/lycoris.png?raw=true");
                        } else {
                            article.setArticleCover(postReq.getArticleCover());
                        }
                        article.setCategoryId(postReq.getCategoryId());
                        article.setArticleTitle(postReq.getArticleTitle());
                        article.setArticleContent(postReq.getArticleContent());
                        article.setUpdateTime(now);
                        article.setIsTop(postReq.getIsTop());
                        article.setIsDraft(postReq.getIsDraft());
                        article.setIsDelete(postReq.getIsDelete());

                        List<Integer> tagList = postReq.getTagIdList();
                            //添加文章标签
                            if (tagList != null) {
                                for (Integer i : tagList) {
                                    ArticleTag at = new ArticleTag();
                                    at.setArticleId(article.getId());
                                    at.setTagId(i);
                                    CODE = ArticleTagDao.insert(at).getCode();
                                }
                            }

                        CODE = ArticleDao.update(article).getCode();

                        if (CODE!=ErrCode.OK.getCode()){
                            result.failure(CODE,ErrMessage.getMsg(CODE));
                        }else {
                            result.success();
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JsonUtil.returnJSON(response,JsonUtil.beanToJson(result));
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收文章id数组进行删除
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 获取request中对数据转换为JSON对象
        JSONObject json = JsonUtil.parseToJson(request);
        // 构建响应数据对象
        Result result = new Result<>();

        if (json!=null) {
            // 使用JSON对象中的数据
            // 将json绑定到AdminDELETEReq对象
            AdminDELETEReq delReq = JSON.toJavaObject(json, AdminDELETEReq.class);
            int[] idList = delReq.getData();
            for (Integer i : idList) {
                CODE = ArticleDao.delete(i).getCode();
                if (CODE!=ErrCode.OK.getCode()){
                    //删除失败
                    break;
                }
            }
            if (CODE==ErrCode.OK.getCode()){
                result.success();
            }else {
                result.failure(CODE,ErrMessage.getMsg(CODE));
            }
        }
        JsonUtil.returnJSON(response,JsonUtil.beanToJson(result));
    }

    @Override
    protected void doPut(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 获取request中对数据转换为JSON对象
        JSONObject json = JsonUtil.parseToJson(request);
        // 构建响应数据对象
        Result result = new Result<>();

        if (json!=null) {
            // 使用JSON对象中的数据
            // 将json绑定到AdminArticlePUTReq对象
            AdminPUTReq putReq = JSON.toJavaObject(json, AdminPUTReq.class);
            int[] idList = putReq.getIdList();

            for (Integer i : idList) {
                CODE = ArticleDao.delete(i).getCode();
                if (CODE!=ErrCode.OK.getCode()){
                    //删除失败
                    break;
                }
            }
            if (CODE==ErrCode.OK.getCode()){
                result.success();
            }else {
                result.failure(CODE,ErrMessage.getMsg(CODE));
            }
        }
        JsonUtil.returnJSON(response,JsonUtil.beanToJson(result));
    }

    /**
     * /articles/options的处理函数
     * @param request
     * @param response
     * @return
     */
    public Result articlesOptions(HttpServletRequest request,HttpServletResponse response){
        Result result = new Result();

        AdminArticlesOptionsVO optionsVO = new AdminArticlesOptionsVO();

        if (request.getParameter("userId")!=null){
            userId = Integer.parseInt(request.getParameter("userId"));
        }
        List<Category> categories = CategoryDao.query(userId,CategoryDao.FINDBYUSERID);
        if (categories.isEmpty()){
            CODE = ErrCode.ERROR_CATEGORY_IS_NULL.getCode();
        }
        List<Tag> tags = TagDao.query(userId,TagDao.FINDBYUSERID);
        if (tags.isEmpty()){
            CODE = ErrCode.ERROR_TAG_IS_NULL.getCode();
        }
        if (CODE!=ErrCode.OK.getCode()){
            result.failure(CODE,ErrMessage.getMsg(CODE));
        }else {
            optionsVO.setCategoryList(categories);
            optionsVO.setTagList(tags);

            result.success(optionsVO);
        }

        return result;
    }

    /**
     * /articles/images的处理函数
     * 将前端上传的封面上传到github仓库并返回下载url
     * @param request
     * @param response
     * @return
     */
    private Result articlesImages(HttpServletRequest request, HttpServletResponse response) {
        Result result = new Result();

        try {
            // 获取上传的文件
            Part filePart = request.getPart("file");

            // 读取文件内容为字节数组
            byte[] fileContent = PicBedutil.inputStreamToByteArray(filePart.getInputStream());

            // 将字节内容转换为 Base64 编码字符串
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            String filename = "cover"+TimeUtil.encodeTime(TimeUtil.getCurrentTime())+".jpg";//加时间戳
            result.success(PicBedutil.uploadToGitHub(encodedString,filename, PicBedutil.token));
        }catch (Exception e){
            e.printStackTrace();
            CODE=ErrCode.FAIL.getCode();
            result.failure(CODE,ErrMessage.getMsg(CODE));
            return result;
        }

        return  result;
    }

    /**
     * /articles/top处理函数
     * 修改文章置顶状态
     * @param request
     * @param response
     * @return
     */
    private Result articlesTop(HttpServletRequest request,HttpServletResponse response){
        Result result = new Result();

        List<Article> articles = ArticleDao.query(articleId,ArticleDao.FINDBYARTID,isDelete,isDraft);
        String isTop = request.getParameter("isTop");

        if (articles.isEmpty()) {
            //文章为空
            CODE = ErrCode.FAIL.getCode();
        }else {
            //更新置顶状态
            articles.get(0).setIsTop(Byte.parseByte(isTop));
            CODE = ArticleDao.update(articles.get(0)).getCode();
        }

        if (CODE!=ErrCode.OK.getCode()){
            result.failure(CODE,ErrMessage.getMsg(CODE));
        }else {
            result.success();
        }

        return result;
    }
}