package com.github.xszhangxiaocuo.servlet.admin;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.xszhangxiaocuo.dao.CategoryDao;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.req.admin.AdminCategoriesPOSTReq;
import com.github.xszhangxiaocuo.entity.req.admin.AdminDELETEReq;
import com.github.xszhangxiaocuo.entity.resp.admin.AdminListGETVO;
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
import java.util.List;

@WebServlet(name = "adminCategories", value = "/admin/categories")
public class AdminCategories extends HttpServlet {
    int CODE;//业务代码
    int CURRENT=0;//当前页数
    int pageSize=5;//默认每一页大小为5
    int count=0;//总记录数
    int categoriesId=0;//分类id
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // 构建响应数据对象
        Result result = new Result<>();
        List<Category> categories;
        try {
//            String pathInfo = request.getPathInfo(); // 获取 URL 中的路径部分
//            // 获取request中对数据转换为JSON对象
//            JSONObject json = JsonUtil.parseToJson(request);

                CURRENT = Integer.parseInt(request.getParameter("current"));
                pageSize = Integer.parseInt(request.getParameter("size"));

                //还没做jwt解析验证用户，先查询全部
                categories = CategoryDao.query(0,CategoryDao.FINDALL,CURRENT,pageSize);
                count = CategoryDao.query(0,CategoryDao.FINDALL).size();
                if (categories.isEmpty()){
                    //分类列表为空
                    CODE= ErrCode.ERROR_ART_IS_NULL.getCode();
                    result.failure(CODE, ErrMessage.getMsg(CODE));
                }else {
                    AdminListGETVO categoriesGETVO = new AdminListGETVO();
                    categoriesGETVO.setRecordList(categories);
                    categoriesGETVO.setCount(count);
                    result.success(categoriesGETVO);
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
            // 将json绑定到AdminCategoriesPOSTReq对象
            AdminCategoriesPOSTReq postReq = JSON.toJavaObject(json, AdminCategoriesPOSTReq.class);
            List<Category> categories = CategoryDao.query(postReq.getId(),CategoryDao.FINDBYCATEGORYID);
            Timestamp now = TimeUtil.getTimeStamp();
            Category category;
            if (categories.isEmpty()) {//分类不存在就插入数据
                category = new Category(now);

                category.setCategoryName(postReq.getCategoryName());
                category.setUserId(postReq.getUserId());

                CODE = CategoryDao.insert(category).getCode();
            }else {//分类已经存在就更新数据
                category = categories.get(0);
                category.setUserId(postReq.getUserId());
                category.setId(postReq.getId());
                category.setCategoryName(postReq.getCategoryName());
                category.setCreateTime(postReq.getCreateTime());

                CODE = CategoryDao.update(category).getCode();

            }
            if (CODE == ErrCode.OK.getCode()) {
                result.success();
            } else {
                result.failure(CODE, ErrMessage.getMsg(CODE));
            }
        }
        JsonUtil.returnJSON(response,JsonUtil.beanToJson(result));
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收分类id数组进行删除
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
                CODE = CategoryDao.delete(i).getCode();
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
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收分类id数组进行删除
    }
}