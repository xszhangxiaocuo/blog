package com.github.xszhangxiaocuo.utils;

import com.github.xszhangxiaocuo.entity.Err.ErrCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBUtil {
    protected Connection conn = null;
    protected PreparedStatement preStmt = null;
    protected ResultSet rs = null;

    public DBUtil(){
        conn = DruidUtil.getConnection();
    }

    public void close(Statement stmt, Connection conn){
        DruidUtil.close(stmt,conn);
    }

    public void close(ResultSet rs, Statement stmt, Connection conn){
        DruidUtil.close(rs,stmt,conn);
    }

}
