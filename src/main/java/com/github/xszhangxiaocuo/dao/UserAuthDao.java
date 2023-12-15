package com.github.xszhangxiaocuo.dao;

import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.sql.UserAuth;
import com.github.xszhangxiaocuo.utils.DBUtil;
import com.mysql.cj.x.protobuf.Mysqlx;
import org.apache.ibatis.jdbc.Null;

import java.math.BigInteger;
import java.util.IdentityHashMap;
import java.util.logging.Logger;


public class UserAuthDao extends DBUtil {
    Logger logger = Logger.getLogger(UserAuthDao.class.getName());

    /**
     * 传入UserAuth对象进行插入，id自增
     * @param data
     * @return
     */
    public ErrCode insert(UserAuth data) {
        try {
            //用户已经存在
            if (query(data.getUsername())!=null){
                return ErrCode.ERROR_USER_NAME_USED;
            }

            String sql = "INSERT INTO user_auth(?,?,?,?,?,?,?,?,?)" +
                    "VALUE(?,?,?,?,?,?,?,?,?)";
            preStmt = conn.prepareStatement(sql);
            preStmt.setString(1,data.createAtName);
            preStmt.setString(2,data.updatedAtName);
            preStmt.setString(3,data.userInfoIdName);
            preStmt.setString(4,data.usernameName);
            preStmt.setString(5,data.passwordName);
            preStmt.setString(6,data.loginTypeName);
            preStmt.setString(7,data.ipAddressName);
            preStmt.setString(8,data.ipSourceName);
            preStmt.setString(9,data.lastLoginTimeName);

            preStmt.setDate(10,data.getCreatedAt());
            preStmt.setDate(11,data.getUpdatedAt());
            preStmt.setObject(12,BigInteger.valueOf(data.getUserInfoId()));
            preStmt.setString(13,data.getUsername());
            preStmt.setString(14,data.getPassword());
            preStmt.setByte(15,data.getLoginType());
            preStmt.setString(16,data.getIpAddress());
            preStmt.setString(17,data.getIpSource());
            preStmt.setDate(18,data.getLastLoginTime());

            int row = preStmt.executeUpdate();
            if (row>0){
                return ErrCode.OK;
            }
        }catch (Exception e){
            logger.severe(e.getMessage());
        }finally {
            super.close(rs,preStmt,conn);
        }
        return ErrCode.FAIL;
    }

    /**
     * 根据username删除
     * @param key
     * @return
     */
    public ErrCode delete(String key) {
        try {
            String sql = "delete from user_auth where username = ?";
            preStmt = conn.prepareStatement(sql);

            preStmt.setString(1,key);

            int row = preStmt.executeUpdate();
            if (row>0){
                return ErrCode.OK;
            }
        }catch (Exception e){
            logger.severe(e.getMessage());
        }finally {
            super.close(rs,preStmt,conn);
        }
        return ErrCode.FAIL;
    }

    /**
     * 直接传入UserAuth对象进行更新
     * @param data
     * @return
     */
    public ErrCode update(UserAuth data) {
        try {
            String sql = "UPDATE user_auth SET " +
                    "?=?,?=?,?=?,?=?,?=?,?=?,?=?,?=?,?=? WHERE username=?";
            preStmt = conn.prepareStatement(sql);
            preStmt.setString(1,data.createAtName);
            preStmt.setString(2,data.updatedAtName);
            preStmt.setString(3,data.userInfoIdName);
            preStmt.setString(4,data.usernameName);
            preStmt.setString(5,data.passwordName);
            preStmt.setString(6,data.loginTypeName);
            preStmt.setString(7,data.ipAddressName);
            preStmt.setString(8,data.ipSourceName);
            preStmt.setString(9,data.lastLoginTimeName);

            preStmt.setDate(10,data.getCreatedAt());
            preStmt.setDate(11,data.getUpdatedAt());
            preStmt.setObject(12,BigInteger.valueOf(data.getUserInfoId()));
            preStmt.setString(13,data.getUsername());
            preStmt.setString(14,data.getPassword());
            preStmt.setByte(15,data.getLoginType());
            preStmt.setString(16,data.getIpAddress());
            preStmt.setString(17,data.getIpSource());
            preStmt.setDate(18,data.getLastLoginTime());

            preStmt.setString(19,data.getUsername());

            int row = preStmt.executeUpdate();
            if (row>0){
                return ErrCode.OK;
            }
        }catch (Exception e){
            logger.severe(e.getMessage());
        }finally {
            super.close(rs,preStmt,conn);
        }
        return ErrCode.FAIL;
    }

    /**
     * 根据username查询数据，没有返回null
     * @param key
     * @return
     */
    public UserAuth query(String key) {
        try {
            String sql = "SELECT * FROM user_auth WHERE username=?";
            preStmt = conn.prepareStatement(sql);
            preStmt.setObject(1,key);

            rs = preStmt.executeQuery();

            while (rs.next()) {
                UserAuth userAuth = new UserAuth();
                userAuth.setId(rs.getLong(userAuth.idName));
                userAuth.setCreatedAt(rs.getDate(userAuth.createAtName));
                userAuth.setUpdatedAt(rs.getDate(userAuth.updatedAtName));
                userAuth.setUserInfoId(rs.getLong(userAuth.userInfoIdName));
                userAuth.setUsername(rs.getString(userAuth.usernameName));
                userAuth.setPassword(rs.getString(userAuth.passwordName));
                userAuth.setLoginType(rs.getByte(userAuth.loginTypeName));
                userAuth.setIpAddress(rs.getString(userAuth.ipAddressName));
                userAuth.setIpSource(rs.getString(userAuth.ipSourceName));
                userAuth.setLastLoginTime(rs.getDate(userAuth.lastLoginTimeName));
                return userAuth;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            super.close(rs,preStmt,conn);
        }
        return null;
    }


}
