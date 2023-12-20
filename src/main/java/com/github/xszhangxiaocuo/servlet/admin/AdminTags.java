package com.github.xszhangxiaocuo.servlet.admin;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.xszhangxiaocuo.dao.CategoryDao;
import com.github.xszhangxiaocuo.dao.TagDao;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.req.admin.AdminCategoriesPOSTReq;
import com.github.xszhangxiaocuo.entity.req.admin.AdminTagDELETEReq;
import com.github.xszhangxiaocuo.entity.req.admin.AdminTagPOSTReq;
import com.github.xszhangxiaocuo.entity.resp.admin.AdminCategoriesGETVO;
import com.github.xszhangxiaocuo.entity.resp.admin.AdminTagGETVO;
import com.github.xszhangxiaocuo.entity.sql.Category;
import com.github.xszhangxiaocuo.entity.sql.Tag;
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

@WebServlet(name = "adminTags", value = "/admin/tags")
public class AdminTags extends HttpServlet {
    int CODE;//业务代码
    int CURRENT=0;//当前页数
    int pageSize=5;//默认每一页大小为5
    int categoriesId=0;//标签id
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // 构建响应数据对象
        Result result = new Result<>();
        List<Tag> tags;
        try {
//            String pathInfo = request.getPathInfo(); // 获取 URL 中的路径部分
//            // 获取request中对数据转换为JSON对象
//            JSONObject json = JsonUtil.parseToJson(request);

                CURRENT = Integer.parseInt(request.getParameter("current"));
                pageSize = Integer.parseInt(request.getParameter("size"));

                //还没做jwt解析验证用户，先查询全部
                tags = TagDao.query(0,TagDao.FINDALL,CURRENT,pageSize);
                if (tags==null||tags.size()==0){
                    //标签列表为空
                    CODE= ErrCode.ERROR_ART_IS_NULL.getCode();
                    result.failure(CODE, ErrMessage.getMsg(CODE));
                }else {
                    AdminTagGETVO tagGETVO = new AdminTagGETVO();
                    tagGETVO.setRecordList(tags);
                    tagGETVO.setCount(tags.size());
                    result.success(tagGETVO);
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
            // 将json绑定到AdminTagPOSTReq对象
            AdminTagPOSTReq postReq = JSON.toJavaObject(json, AdminTagPOSTReq.class);
            List<Tag> tags = TagDao.query(postReq.getId(),TagDao.FINDBYTAGID);
            Timestamp now = TimeUtil.getTimeStamp();
            Tag tag;
            if (tags.isEmpty()) {//标签不存在就插入数据
                tag = new Tag(now);

                tag.setTagName(postReq.getTagName());
                tag.setUserId(postReq.getUserId());

                CODE = TagDao.insert(tag).getCode();
            }else {//标签已经存在就更新数据
                tag = tags.get(0);
                tag.setUserId(postReq.getUserId());
                tag.setId(postReq.getId());
                tag.setTagName(postReq.getTagName());
                tag.setCreateTime(postReq.getCreateTime());

                CODE = TagDao.update(tag).getCode();

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
        //接收标签id数组进行删除
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 获取request中对数据转换为JSON对象
        JSONObject json = JsonUtil.parseToJson(request);
        // 构建响应数据对象
        Result result = new Result<>();

        if (json!=null) {
            // 使用JSON对象中的数据
            // 将json绑定到AdminTagDELETEReq对象
            AdminTagDELETEReq delReq = JSON.toJavaObject(json, AdminTagDELETEReq.class);
            int[] tagIdList = delReq.getData();
            for (Integer i : tagIdList) {
                CODE = TagDao.delete(i).getCode();
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
        //接收标签id数组进行删除
    }
}