package com.github.xszhangxiaocuo.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    public static Timestamp getTimeStamp(){
        //获取当前时间
        return Timestamp.valueOf(LocalDateTime.now());
    }

    public static String getCurrentTime(){
        // 获取当前日期和时间
        LocalDateTime now = LocalDateTime.now();

        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化当前日期时间
        String formattedNow = now.format(formatter);

        return formattedNow;
    }

    public static String encodeTime(String time){
        try {
            // 对日期时间部分进行编码
            String encodedDateTime = URLEncoder.encode(time, StandardCharsets.UTF_8.toString());

           return encodedDateTime;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
