package com.github.xszhangxiaocuo.utils;

import com.alibaba.druid.pool.ha.PropertiesUtils;

import java.io.IOException;
import java.util.Properties;

/**
读取properties配置文件
 */
public class PropertiesUtil {
    static Properties property = new Properties();
    public static boolean loadFile(String fileName){
        try {
            //filename为项目下的类路径中的资源，即src目录下的路径
            property.load(PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static String getPropertyValue(String key) {
        return property.getProperty(key);
    }
}
