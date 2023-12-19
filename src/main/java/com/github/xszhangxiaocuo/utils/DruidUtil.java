package com.github.xszhangxiaocuo.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class DruidUtil {
    private static DataSource ds=null;

    static Logger logger = Logger.getLogger(DruidUtil.class.getName());
    static{
        try {
            logger.info("开始连接数据库。。。");
            //读取数据库连接配置文件
            PropertiesUtil.loadFile("config/druid.properties");

            ds = DruidDataSourceFactory.createDataSource(PropertiesUtil.property);
            logger.info("数据库连接成功！");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static Connection getConnection(){
        Connection conn = null;
        try {
            conn = ds.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(conn==null){
            logger.severe("conn is null!");
        }
        return conn;
    }

    public static void close(Statement stmt, Connection conn){
        if (stmt!=null){
            try {
                stmt.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
            stmt = null;
        }
        if (conn!=null){
            try {
                conn.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
            conn = null;
        }
    }

    public static void close(ResultSet rs, Statement stmt, Connection conn){
        if (rs!=null){
            try {
                rs.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
            rs = null;
        }
        close(stmt,conn);
    }
}
