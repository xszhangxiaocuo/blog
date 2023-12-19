package com.github.xszhangxiaocuo.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

public class JsonUtil<T> {
    static Logger logger = Logger.getLogger(JsonUtil.class.getName());
    /**
    *读取请求体中的数据，转换为JSONObject
     */
    public static JSONObject parseToJson(HttpServletRequest request) {
        JSONObject json=null;
        // 读取请求体中的数据
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {//读取 HTTP 请求的主体(body)
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            //将request中的json字符串解析为JSONObject
            json = JSONObject.parseObject(sb.toString());

        }catch (Exception e){
            logger.severe("json转换失败！");
        }

        // 将读取的数据转换为JSON对象
        return json;
    }
    /**
    *将json数据写入响应
     */
    public static void returnJSON(HttpServletResponse response,Object data) {
        try(PrintWriter out = response.getWriter()) {
            out.print(data.toString()); // 将JSON对象作为字符串写入响应
            out.flush(); // 清空输出流，确保所有数据都被发送
        }catch (IOException e){
            logger.severe("json写入响应失败！");
        }
    }
    /**
    *将javabean转换为json
     */
    public static Object beanToJson(Object object){
        String json = JSON.toJSONString(object);
        if (json.startsWith("[")) {
            // 如果是数组，返回 JSONArray
            return JSON.parseArray(json);
        } else {
            // 否则，返回 JSONObject
            return JSON.parseObject(json);
        }
    }
}
