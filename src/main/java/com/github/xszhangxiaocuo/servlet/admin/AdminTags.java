package com.github.xszhangxiaocuo.servlet.admin;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.xszhangxiaocuo.dao.TagDao;
import com.github.xszhangxiaocuo.dao.UserInfoDao;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.req.admin.AdminDELETEReq;
import com.github.xszhangxiaocuo.entity.req.admin.AdminTagPOSTReq;
import com.github.xszhangxiaocuo.entity.resp.admin.AdminListGETVO;
import com.github.xszhangxiaocuo.entity.sql.Tag;
import com.github.xszhangxiaocuo.entity.sql.UserInfo;
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

@WebServlet(name = "adminTags", value = "/admin/tags")
public class AdminTags extends HttpServlet {
    int CODE;//业务代码
    int CURRENT=0;//当前页数
    int pageSize=5;//默认每一页大小为5
    int count=0;//总记录数
    int categoriesId=0;//标签id
    int userId = 0;//用户id
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // 构建响应数据对象
        Result result = new Result<>();
        List<Tag> tags = new ArrayList<>();
        try {
//            String pathInfo = request.getPathInfo(); // 获取 URL 中的路径部分
//            // 获取request中对数据转换为JSON对象
//            JSONObject json = JsonUtil.parseToJson(request);

                CURRENT = Integer.parseInt(request.getParameter("current"));
                pageSize = Integer.parseInt(request.getParameter("size"));

            if (request.getParameter("userId") != null) {
                userId = Integer.parseInt(request.getParameter("userId"));
            }
            UserInfo userInfo = UserInfoDao.query(userId);
            if (userInfo == null) {
                CODE = ErrCode.ERROR_USER_NOT_EXIST.getCode();
                result.failure(CODE, ErrMessage.getMsg(CODE));
            } else if (userInfo.getUserRole().equals("管理员")) {
                //管理员查询所有标签
                tags = TagDao.query(0, TagDao.FINDALL, CURRENT, pageSize);
                count = TagDao.query(0, TagDao.FINDALL).size();

            }else if (userInfo.getUserRole().equals("普通用户")){
                //用户查询自己的标签
                tags = TagDao.query(userId,TagDao.FINDBYUSERID,CURRENT,pageSize);
                count = TagDao.query(userId,TagDao.FINDBYUSERID).size();
            }
            if (tags == null || tags.size() == 0) {
                //标签列表为空
                CODE = ErrCode.ERROR_ART_IS_NULL.getCode();
                result.failure(CODE, ErrMessage.getMsg(CODE));
            } else {
                AdminListGETVO tagGETVO = new AdminListGETVO();
                tagGETVO.setRecordList(tags);
                tagGETVO.setCount(count);
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
            // 将json绑定到AdminDELETEReq对象
            AdminDELETEReq delReq = JSON.toJavaObject(json, AdminDELETEReq.class);
            int[] idList = delReq.getData();
            for (Integer i : idList) {
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

}