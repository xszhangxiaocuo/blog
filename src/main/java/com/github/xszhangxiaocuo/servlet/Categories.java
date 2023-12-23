package com.github.xszhangxiaocuo.servlet;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.xszhangxiaocuo.dao.ArticleDao;
import com.github.xszhangxiaocuo.dao.CategoryDao;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.req.admin.AdminCategoriesPOSTReq;
import com.github.xszhangxiaocuo.entity.req.admin.AdminDELETEReq;
import com.github.xszhangxiaocuo.entity.resp.CategoriesVO;
import com.github.xszhangxiaocuo.entity.resp.ListVO;
import com.github.xszhangxiaocuo.entity.resp.admin.AdminListGETVO;
import com.github.xszhangxiaocuo.entity.sql.Article;
import com.github.xszhangxiaocuo.entity.sql.Category;
import com.github.xszhangxiaocuo.utils.JsonUtil;
import com.github.xszhangxiaocuo.utils.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Categories", value = "/categories")
public class Categories extends HttpServlet {
    int CODE;//业务代码
    int count=0;//总记录数
    int userId=0;//用户id
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // 构建响应数据对象
        Result result = new Result<>();
        List<Category> categories;
        List<CategoriesVO> list = new ArrayList<>();
        userId=0;
        try {
                if (request.getParameter("userId")!=null){
                    userId = Integer.parseInt(request.getParameter("userId"));
                }
                categories = CategoryDao.query(userId,CategoryDao.FINDBYUSERID);
                count = CategoryDao.query(userId,CategoryDao.FINDBYUSERID).size();
                if (categories.isEmpty()){
                    //分类列表为空
                    CODE= ErrCode.ERROR_CATEGORY_IS_NULL.getCode();
                    result.failure(CODE, ErrMessage.getMsg(CODE));
                }else {
                    for (Category category : categories) {
                        CategoriesVO categoriesVO = new CategoriesVO();
                        categoriesVO.setId(category.getId());
                        categoriesVO.setCategoryName(category.getCategoryName());
                        int articleCount = 0;
                        articleCount = ArticleDao.queryByCategory(userId,category.getId(),(byte)0,(byte)0).size();
                        categoriesVO.setArticleCount(articleCount);

                        list.add(categoriesVO);
                    }
                    ListVO listVO = new ListVO();
                    listVO.setRecordList(list);
                    listVO.setCount(count);
                    result.success(listVO);
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