package com.github.xszhangxiaocuo.servlet.admin;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.xszhangxiaocuo.dao.ArticleDao;
import com.github.xszhangxiaocuo.dao.CategoryDao;
import com.github.xszhangxiaocuo.dao.TagDao;
import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.Err.ErrMessage;
import com.github.xszhangxiaocuo.entity.Result;
import com.github.xszhangxiaocuo.entity.req.admin.AdminArticlesPOSTReq;
import com.github.xszhangxiaocuo.entity.resp.admin.AdminArticlesOptionsVO;
import com.github.xszhangxiaocuo.entity.resp.admin.AdminListGETVO;
import com.github.xszhangxiaocuo.entity.sql.Article;
import com.github.xszhangxiaocuo.entity.sql.Tag;
import com.github.xszhangxiaocuo.utils.JsonUtil;
import com.github.xszhangxiaocuo.utils.PicBedutil;
import com.github.xszhangxiaocuo.utils.TimeUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "adminArticles", value = "/admin/articles/*")
@MultipartConfig//处理多部分请求
public class AdminArticles extends HttpServlet {
    int CODE;//业务代码
    int CURRENT=0;//当前页数
    int pageSize=5;//默认每一页大小为5
    int count=0;//总记录数
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
            //JSONObject json = JsonUtil.parseToJson(request);

            if (pathInfo != null&&!pathInfo.isEmpty()) {//不为空表示获取特定文章信息
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
                    articles = ArticleDao.query(articleId, ArticleDao.FINDBYARTID);
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

                //还没做jwt解析验证用户，先查询全部
                articles = ArticleDao.query(0,ArticleDao.FINDALL,CURRENT,pageSize);
                count = ArticleDao.query(0,ArticleDao.FINDALL).size();
                if (articles.isEmpty()){
                    //文章列表为空
                    CODE= ErrCode.ERROR_ART_IS_NULL.getCode();
                    result.failure(CODE, ErrMessage.getMsg(CODE));
                }else {
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

        if (pathInfo != null&&!pathInfo.isEmpty()) {//不为空表示获取特定文章信息
            // 去除开头的斜杠（如果存在）
            if (pathInfo.startsWith("/")) {
                pathInfo = pathInfo.substring(1);
            }

            // 使用“/”分割路径
            String[] pathParts = pathInfo.split("/");

          if (pathParts[0].equals("images")) {
                result = articlesImages(request, response);
            }
        }else {

        // 获取request中对数据转换为JSON对象
        JSONObject json = JsonUtil.parseToJson(request);
        if (json!=null) {
            // 使用JSON对象中的数据
            // 将json绑定到AdminArticlesPOSTReq对象
            AdminArticlesPOSTReq postReq = JSON.toJavaObject(json, AdminArticlesPOSTReq.class);
            List<Article> articles = ArticleDao.query(postReq.getId(), ArticleDao.FINDBYARTID);
            Timestamp now = TimeUtil.getTimeStamp();
            if (articles.isEmpty()) {//文章不存在就插入数据
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
            } else {//文章已经存在就更新数据
                Article article = articles.get(0);
                article.setUserId(postReq.getUserId());
                if (postReq.getArticleCover() == null || postReq.getArticleCover().isEmpty()) {
                    article.setArticleCover("https://github.com/xszhangxiaocuo/picBed/blob/master/picBed/lycoris.png?raw=true");
                } else {
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

    /**
     * /articles/options的处理函数
     * @param request
     * @param response
     * @return
     */
    public Result articlesOptions(HttpServletRequest request,HttpServletResponse response){
        Result result = new Result();

        AdminArticlesOptionsVO optionsVO = new AdminArticlesOptionsVO();
        //需要根据userid查询，暂时先查询全部
        optionsVO.setCategoryList(CategoryDao.query(0,CategoryDao.FINDALL));
        optionsVO.setTagList(TagDao.query(0,TagDao.FINDALL));

        result.success(optionsVO);

        return result;
    }

    /**
     * /articles/images的处理函数
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
            byte[] fileContent = inputStreamToByteArray(filePart.getInputStream());

            // 将字节内容转换为 Base64 编码字符串
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            String filename = "cover"+TimeUtil.encodeTime(TimeUtil.getCurrentTime())+".jpg";
            result.success(uploadToGitHub(encodedString,filename, PicBedutil.token));
        }catch (Exception e){
            e.printStackTrace();
            CODE=ErrCode.FAIL.getCode();
            result.failure(CODE,ErrMessage.getMsg(CODE));
            return result;
        }

        return  result;
    }

    private byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

    public String uploadToGitHub(String base64Content, String filePath, String token) throws IOException {
        String repo = "xszhangxiaocuo/picBed"; // GitHub 仓库名
        String uploadUrl = "https://api.github.com/repos/" + repo + "/contents/picBed" + filePath; // API URL
        System.out.println(uploadUrl);

        // 创建请求体
        Map<String, Object> data = new HashMap<>();
        data.put("message", "Upload file");
        data.put("content", base64Content);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(data);

        // 发送 POST 请求到 GitHub
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(uploadUrl);
            request.setHeader("Authorization", "Bearer " + token);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept","application/vnd.github+json");
            request.setHeader("X-GitHub-Api-Version","2022-11-28");
            request.setHeader("Accept-Encoding","base64");
            request.setEntity(new StringEntity(json));

            try (CloseableHttpResponse response = client.execute(request)) {
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity);

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseString);

                // 提取 download_url 字段
                JsonNode downloadUrlNode = rootNode.path("content").path("download_url");
                String downloadUrl = downloadUrlNode.asText();

                System.out.println(downloadUrl);
                return downloadUrl;
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}