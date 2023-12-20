package com.github.xszhangxiaocuo.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TimeUtil {
    public static Timestamp getTimeStamp(){
        //获取当前时间
        return Timestamp.valueOf(LocalDateTime.now());
    }
}
