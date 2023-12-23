package com.github.xszhangxiaocuo.servlet.admin;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.xszhangxiaocuo.dao.ArticleDao;
import com.github.xszhangxiaocuo.dao.CommentDao;
import com.github.xszhangxiaocuo.dao.UserInfoDao;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.req.CommentsReq;
import com.github.xszhangxiaocuo.entity.req.admin.AdminDELETEReq;
import com.github.xszhangxiaocuo.entity.req.admin.AdminPUTReq;
import com.github.xszhangxiaocuo.entity.resp.ListVO;
import com.github.xszhangxiaocuo.entity.sql.Article;
import com.github.xszhangxiaocuo.entity.sql.Comment;
import com.github.xszhangxiaocuo.entity.sql.UserInfo;
import com.github.xszhangxiaocuo.utils.JsonUtil;
import com.github.xszhangxiaocuo.utils.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "adminComments", value = "/admin/comments")
public class AdminComments extends HttpServlet {
    int CODE;
    int count = 0;
    int current=0;
    int pageSize=10;
    int userId=0;
    byte isDelete = 0;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Result result = new Result();

         count = 0;
         current=0;
         pageSize=10;
         userId=0;
         isDelete = 0;
        try {
            current = Integer.parseInt(request.getParameter("current"));
            pageSize = Integer.parseInt(request.getParameter("size"));
            isDelete = Byte.parseByte(request.getParameter("isDelete"));
            String param = request.getParameter("userId");
            if (param != null || !param.isEmpty()) {
                userId = Integer.parseInt(param);
            }

            ListVO listVO = new ListVO();
            List<Comment> comments = new ArrayList<>();
            UserInfo userInfo = UserInfoDao.query(userId);
            if (userInfo == null) {
                CODE = ErrCode.ERROR_USER_NOT_EXIST.getCode();
                result.failure(CODE, ErrMessage.getMsg(CODE));
            } else {
                String userRole = userInfo.getUserRole();
                if (userRole.equals("管理员")) {
                    //管理员查询所有文章评论
                    count = CommentDao.query(0, CommentDao.FINDALL, isDelete).size();
                    comments = CommentDao.query(0, CommentDao.FINDALL, isDelete, current, pageSize);

                    if (comments.isEmpty()) {
                        CODE = ErrCode.ERROR_COMMENT_IS_NULL.getCode();
                        result.failure(CODE, ErrMessage.getMsg(CODE));
                    } else {
                        listVO.setRecordList(comments);

                        listVO.setCount(count);
                        result.success(listVO);
                    }
                }else if (userRole.equals("普通用户")){
                    //普通用户只能查询自己的文章评论
                    //先查询该用户的所有文章id
                    List<Article> userArticles = ArticleDao.query(userId,ArticleDao.FINDBYUSERID,(byte)0,(byte)0);
                    if (!userArticles.isEmpty()){
                        //根据文章id获取相应的评论
                        for (Article userArticle : userArticles) {
                            List<Comment> temp = CommentDao.query(userArticle.getId(), CommentDao.FINDBYARTID, isDelete);
                            comments.addAll(temp);
                        }
                        count = comments.size();

                        if (comments.isEmpty()) {
                            CODE = ErrCode.ERROR_COMMENT_IS_NULL.getCode();
                            result.failure(CODE, ErrMessage.getMsg(CODE));
                        } else {
                            int start, end;
                            start = (current - 1) * pageSize;
                            end = start + pageSize;
                            if (start >= count) {
                                //当前请求页为空
                                CODE = ErrCode.ERROR_COMMENT_IS_NULL.getCode();
                                result.failure(CODE, ErrMessage.getMsg(CODE));
                            } else {
                                if (end > count) {
                                    end = count;
                                }
                                listVO.setRecordList(comments.subList(start,end));

                                listVO.setCount(count);
                                result.success(listVO);
                            }
                        }

                    }else {
                        CODE = ErrCode.ERROR_ART_IS_NULL.getCode();
                        result.failure(CODE,ErrMessage.getMsg(CODE));
                    }

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

        Result result = new Result();
        JSONObject json = JsonUtil.parseToJson(request);
        try {
                CommentsReq commentsReq = JSON.toJavaObject(json, CommentsReq.class);
                Comment comment = new Comment(TimeUtil.getTimeStamp());
                comment.setUserId(commentsReq.getUserID());
                comment.setArrticleId(commentsReq.getArticleId());
                comment.setCommentContent(commentsReq.getCommentContent());
                comment.setIsDelete((byte) 0);

                CODE = CommentDao.insert(comment).getCode();
                if (CODE!=ErrCode.OK.getCode()){
                    result.failure(CODE,ErrMessage.getMsg(CODE));
                }else {
                    result.success();
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
                CODE = CommentDao.delete(i).getCode();
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
            isDelete = putReq.getIsDelete();
            for (Integer i : idList) {
                //如果传过来的isDelete为1，说明是删除操作，0为恢复操作
                Comment comment = CommentDao.query(i,CommentDao.FINDBYCOMMENTID, (byte) (1-isDelete)).get(0);
                comment.setIsDelete(isDelete);
                CODE = CommentDao.update(comment).getCode();
                if (CODE!=ErrCode.OK.getCode()){
                    //恢复失败
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
}