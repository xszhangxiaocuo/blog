package com.github.xszhangxiaocuo.servlet;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPObject;
import com.github.xszhangxiaocuo.dao.ArticleDao;
import com.github.xszhangxiaocuo.dao.CommentDao;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.req.CommentsReq;
import com.github.xszhangxiaocuo.entity.resp.ArticleVO;
import com.github.xszhangxiaocuo.entity.resp.ListVO;
import com.github.xszhangxiaocuo.entity.sql.Article;
import com.github.xszhangxiaocuo.entity.sql.Comment;
import com.github.xszhangxiaocuo.utils.JsonUtil;
import com.github.xszhangxiaocuo.utils.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.peer.CanvasPeer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Comments", value = "/comments")
public class Comments extends HttpServlet {
    int CODE;
    int count = 0;
    int current=0;
    int pageSize=10;
    int articleId=0;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Result result = new Result();

        current = Integer.parseInt(request.getParameter("current"));
        articleId = Integer.parseInt(request.getParameter("articleId"));

        ListVO listVO = new ListVO();
        List<Comment> comments;
        comments = CommentDao.query(articleId,CommentDao.FINDBYARTID,(byte) 0,current,pageSize);

        if (comments.isEmpty()){
            CODE = ErrCode.ERROR_COMMENT_IS_NULL.getCode();
            result.failure(CODE, ErrMessage.getMsg(CODE));
        }else {
            listVO.setRecordList(comments);
            count = CommentDao.query(articleId,CommentDao.FINDBYARTID,(byte)0).size();
            listVO.setCount(count);
            result.success(listVO);
        }


        JsonUtil.returnJSON(response, JsonUtil.beanToJson(result));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String[] keyword = {"妈的","操","日","逼","fuck"};

        Result result = new Result();
        JSONObject json = JsonUtil.parseToJson(request);
        try {
                CommentsReq commentsReq = JSON.toJavaObject(json, CommentsReq.class);
                Comment comment = new Comment(TimeUtil.getTimeStamp());
                String content = commentsReq.getCommentContent();
            for (String key : keyword) {
                if (content.contains(key)){
                    content = content.replace(key,"**");
                }
            }
                comment.setUserId(commentsReq.getUserID());
                comment.setArrticleId(commentsReq.getArticleId());
                comment.setCommentContent(content);
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
}