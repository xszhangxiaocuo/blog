package com.github.xszhangxiaocuo.servlet.admin;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.xszhangxiaocuo.dao.*;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.req.admin.AdminArticlesPOSTReq;
import com.github.xszhangxiaocuo.entity.req.admin.AdminDELETEReq;
import com.github.xszhangxiaocuo.entity.req.admin.AdminPUTReq;
import com.github.xszhangxiaocuo.entity.req.admin.AdminUsersRolePUTReq;
import com.github.xszhangxiaocuo.entity.resp.admin.AdminArticlesOptionsVO;
import com.github.xszhangxiaocuo.entity.resp.admin.AdminListGETVO;
import com.github.xszhangxiaocuo.entity.resp.admin.AdminUsersVO;
import com.github.xszhangxiaocuo.entity.sql.Article;
import com.github.xszhangxiaocuo.entity.sql.ArticleTag;
import com.github.xszhangxiaocuo.entity.sql.UserAuth;
import com.github.xszhangxiaocuo.entity.sql.UserInfo;
import com.github.xszhangxiaocuo.utils.JsonUtil;
import com.github.xszhangxiaocuo.utils.PicBedutil;
import com.github.xszhangxiaocuo.utils.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.swing.text.rtf.RTFEditorKit;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@WebServlet(name = "adminUsers", value = "/admin/users/*")
@MultipartConfig//处理多部分请求
public class AdminUsers extends HttpServlet {
    int CODE;//业务代码
    int CURRENT=0;//当前页数
    int pageSize=5;//默认每一页大小为5
    int count=0;//总记录数
    int userId=0;//用户id
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // 构建响应数据对象
        Result result = new Result<>();
        List<UserInfo> userInfos;
        try {

                CURRENT = Integer.parseInt(request.getParameter("current"));
                pageSize = Integer.parseInt(request.getParameter("size"));
                //还没做jwt解析验证用户，先查询全部
                userInfos = UserInfoDao.query(CURRENT,pageSize);

                count = UserInfoDao.query().size();
                if (userInfos.isEmpty()){
                    //用户列表为空
                    CODE= ErrCode.ERROR_ART_IS_NULL.getCode();
                    result.failure(CODE, ErrMessage.getMsg(CODE));
                }else {
                    AdminListGETVO listGETVO = new AdminListGETVO();
                    List<AdminUsersVO> usersList = new ArrayList<>();

                    for (UserInfo info : userInfos) {
                        AdminUsersVO usersVO = new AdminUsersVO();
                        usersVO.setId(info.getId());
                        usersVO.setUserRole(info.getUserRole());
                        usersVO.setNickname(info.getNickname());
                        usersVO.setAvatar(info.getAvatar());
                        usersVO.setIntro(info.getIntro());
                        usersVO.setWebsite(info.getWebsite());
                        usersVO.setCreateTime(info.getCreateTime());
                        usersVO.setIsSilence(info.getIsSilence());
                        usersVO.setBlogTitle(info.getBlogTitle());

                        UserAuth userAuth = UserAuthDao.query(info.getId());
                        if (userAuth!=null) {
                            usersVO.setEmail(userAuth.getUsername());//username就是email
                            usersVO.setLastLoginTime(userAuth.getLastLoginTime());
                        }
                        usersList.add(usersVO);
                    }
                    listGETVO.setRecordList(usersList);
                    listGETVO.setCount(UserInfoDao.query().size());

                    result.success(listGETVO);

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
        try {
            if (pathInfo != null && !pathInfo.isEmpty()) {//不为空表示获取特定文章信息
                // 去除开头的斜杠（如果存在）
                if (pathInfo.startsWith("/")) {
                    pathInfo = pathInfo.substring(1);
                }

                // 使用“/”分割路径
                String[] pathParts = pathInfo.split("/");

                if (pathParts[0].equals("avatar")) {
                    result = usersAvatar(request, response);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            JsonUtil.returnJSON(response,JsonUtil.beanToJson(result));
        }
    }

    @Override
    protected void doPut(HttpServletRequest request,HttpServletResponse response)throws ServletException, IOException{
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo(); // 获取 URL 中的路径部分
        Result result = new Result();

        if (pathInfo != null&&!pathInfo.isEmpty()&&!pathInfo.startsWith("?")) {//不为空表示获取特定用户信息
            // 去除开头的斜杠（如果存在）
            if (pathInfo.startsWith("/")) {
                pathInfo = pathInfo.substring(1);
            }

            // 使用“/”分割路径
            String[] pathParts = pathInfo.split("/");

            if (pathParts[0].equals("role")) {
                result = usersRole(request,response);
            } else if (pathParts[0].equals("comment")) {
                userId = Integer.parseInt(pathParts[1]);
                result = usersComment(request,response);
            }else if (pathParts[0].equals("info")){
                result = usersInfo(request,response);
            }else if(pathParts[0].equals("password")){
                result = usersPassword(request,response);
            }
        }
        JsonUtil.returnJSON(response,JsonUtil.beanToJson(result));
    }

    /**
     * /users/role的处理函数
     * @param request
     * @param response
     * @return
     */
    public Result usersRole(HttpServletRequest request,HttpServletResponse response){
        Result result = new Result();
        JSONObject json = JsonUtil.parseToJson(request);
        AdminUsersRolePUTReq putReq = JSON.toJavaObject(json,AdminUsersRolePUTReq.class);
        userId = putReq.getId();

        UserInfo userInfo = UserInfoDao.query(userId);
        if (userInfo==null){
            CODE = ErrCode.FAIL.getCode();
            result.failure(CODE,ErrMessage.getMsg(CODE));
        }else {
            userInfo.setNickname(putReq.getNickname());
            userInfo.setUserRole(putReq.getUserRole());
            UserInfoDao.update(userInfo);
            result.success();
        }

        return result;
    }

    /**
     * /users/comment处理函数
     * 修改用户置顶状态
     * @param request
     * @param response
     * @return
     */
    private Result usersComment(HttpServletRequest request,HttpServletResponse response){
        Result result = new Result();

        try {
            UserInfo userInfo = UserInfoDao.query(userId);
            JSONObject json = JsonUtil.parseToJson(request);
            byte isSilence = json.getByte("isSilence");

            if (userInfo==null) {
                //用户不存在
                CODE = ErrCode.ERROR_USER_NOT_EXIST.getCode();
            } else {
                //更新禁言状态
                userInfo.setIsSilence(isSilence);
                CODE = UserInfoDao.update(userInfo).getCode();
            }

            if (CODE != ErrCode.OK.getCode()) {
                result.failure(CODE, ErrMessage.getMsg(CODE));
            } else {
                result.success();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * /users/info的处理函数
     * @return
     */
    private Result usersInfo(HttpServletRequest request,HttpServletResponse response){
        Result result = new Result();

        try {
            JSONObject json = JsonUtil.parseToJson(request);
            String intro = json.getString("intro");
            String blogTitle = json.getString("blogTitle");
            String webSite = json.getString("webSite");
            String nickname = json.getString("nickname");

            userId = json.getInteger("userId");
            UserInfo userInfo = UserInfoDao.query(userId);

            if (userInfo==null) {
                //用户不存在
                CODE = ErrCode.ERROR_USER_NOT_EXIST.getCode();
            } else {
                //更新用户信息
                userInfo.setIntro(intro);
                userInfo.setBlogTitle(blogTitle);
                userInfo.setWebsite(webSite);
                userInfo.setNickname(nickname);
                CODE = UserInfoDao.update(userInfo).getCode();
            }

            if (CODE != ErrCode.OK.getCode()) {
                result.failure(CODE, ErrMessage.getMsg(CODE));
            } else {
                result.success();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * /users/password的处理函数
     * @return
     */
    private Result usersPassword(HttpServletRequest request,HttpServletResponse response){
        Result result = new Result();

        try {
            JSONObject json = JsonUtil.parseToJson(request);
            String password = json.getString("newPassword");

            userId = json.getInteger("userId");
            UserAuth userAuth = UserAuthDao.query(userId);

            if (userAuth==null) {
                //用户不存在
                CODE = ErrCode.ERROR_USER_NOT_EXIST.getCode();
            } else {
                //更新用户信息
                userAuth.setPassword(password);
                CODE = UserAuthDao.update(userAuth).getCode();
            }

            if (CODE != ErrCode.OK.getCode()) {
                result.failure(CODE, ErrMessage.getMsg(CODE));
            } else {
                result.success();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * /users/avatar的处理函数
     * 将前端上传的封面上传到github仓库并返回下载url
     * @param request
     * @param response
     * @return
     */
    private Result usersAvatar(HttpServletRequest request, HttpServletResponse response) {
        Result result = new Result();

        try {
            // 获取上传的文件
            Part filePart = request.getPart("file");


            // 读取文件内容为字节数组
            byte[] fileContent = PicBedutil.inputStreamToByteArray(filePart.getInputStream());

            // 将字节内容转换为 Base64 编码字符串
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            String filename = "avatar"+TimeUtil.encodeTime(TimeUtil.getCurrentTime())+".jpg";//加时间戳
            String url = PicBedutil.uploadToGitHub(encodedString,filename, PicBedutil.token);
            if (url!=null&&!url.isEmpty()) {
                userId = Integer.parseInt(request.getParameter("userId"));
                UserInfo userInfo = UserInfoDao.query(userId);
                if (userInfo!=null) {
                    userInfo.setAvatar(url);
                    UserInfoDao.update(userInfo);
                }
                result.success(url);
            }
        }catch (Exception e){
            e.printStackTrace();
            CODE=ErrCode.FAIL.getCode();
            result.failure(CODE,ErrMessage.getMsg(CODE));
            return result;
        }

        return  result;
    }
}