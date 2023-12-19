package com.github.xszhangxiaocuo.utils;

import com.github.xszhangxiaocuo.entity.Err.ErrCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBUtil {
    public Connection conn = null;
    public PreparedStatement preStmt = null;
    public ResultSet rs = null;

    public void getConnection(){
        conn = DruidUtil.getConnection();
    }

    public void close(){
        DruidUtil.close(rs,preStmt,conn);
    }

}
