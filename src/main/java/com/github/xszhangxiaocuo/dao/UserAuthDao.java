package com.github.xszhangxiaocuo.dao;

import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.sql.UserAuth;
import com.github.xszhangxiaocuo.utils.DBUtil;

import java.math.BigInteger;
import java.util.logging.Logger;


public class UserAuthDao {
    static Logger logger = Logger.getLogger(UserAuthDao.class.getName());

    /**
     * 传入UserAuth对象进行插入，id自增
     * @param data
     * @return
     */
    public static ErrCode insert(UserAuth data) {
        DBUtil db = new DBUtil();//数据库连接
        try {
            //用户已经存在
            if (query(data.getUsername())!=null){
                return ErrCode.ERROR_USER_NAME_USED;
            }
            db.getConnection();

            String sql = "INSERT INTO user_auth(user_info_id, username, password, login_type, ip_addr, ip_source, create_time,last_login_time) " +
                    "VALUES(?,?,?,?,?,?,?,?)";
            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setInt(1, data.getUserInfoId());
            db.preStmt.setString(2, data.getUsername());
            db.preStmt.setString(3, data.getPassword());
            db.preStmt.setByte(4, data.getLoginType());
            db.preStmt.setString(5, data.getIpAddress());
            db.preStmt.setString(6, data.getIpSource());
            db.preStmt.setTimestamp(7, data.getCreateTime());
            db.preStmt.setTimestamp(8, data.getLastLoginTime());

            int row = db.preStmt.executeUpdate();
            if (row>0){
                return ErrCode.OK;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return ErrCode.FAIL;
    }

    /**
     * 根据username删除
     * @param key
     * @return
     */
    public static ErrCode delete(String key) {
        DBUtil db = new DBUtil();//数据库连接
        try {
            db.getConnection();
            String sql = "delete from user_auth where username = ?";
            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setString(1,key);

            int row = db.preStmt.executeUpdate();
            if (row>0){
                return ErrCode.OK;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return ErrCode.FAIL;
    }

    /**
     * 直接传入UserAuth对象进行更新
     * @param data
     * @return
     */
    public static ErrCode update(UserAuth data) {
        DBUtil db = new DBUtil();//数据库连接
        try {
            db.getConnection();
            String sql = "UPDATE user_auth SET " +
                    "user_info_id=?, username=?, password=?, login_type=?, ip_addr=?, ip_source=?, create_time=?,last_login_time=? " +
                    "WHERE username=?";
            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setObject(1, BigInteger.valueOf(data.getUserInfoId()));
            db.preStmt.setString(2, data.getUsername());
            db.preStmt.setString(3, data.getPassword());
            db.preStmt.setByte(4, data.getLoginType());
            db.preStmt.setString(5, data.getIpAddress());
            db.preStmt.setString(6, data.getIpSource());
            db.preStmt.setTimestamp(7, data.getCreateTime());
            db.preStmt.setTimestamp(8, data.getLastLoginTime());

            db.preStmt.setString(9, data.getUsername());

            int row = db.preStmt.executeUpdate();
            if (row>0){
                return ErrCode.OK;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return ErrCode.FAIL;
    }

    /**
     * 根据username查询数据，没有返回null
     * @param key
     * @return
     */
    public static UserAuth query(String key) {
        DBUtil db = new DBUtil();//数据库连接
        try {
            db.getConnection();
            String sql = "SELECT * FROM user_auth WHERE username=?";
            db.preStmt = db.conn.prepareStatement(sql);
            db.preStmt.setObject(1,key);

            db.rs = db.preStmt.executeQuery();

            while (db.rs.next()) {
                UserAuth userAuth = new UserAuth();
                userAuth.setId(db.rs.getInt(UserAuth.idName));
                userAuth.setUserInfoId(db.rs.getInt(UserAuth.userInfoIdName));
                userAuth.setUsername(db.rs.getString(UserAuth.usernameName));
                userAuth.setPassword(db.rs.getString(UserAuth.passwordName));
                userAuth.setLoginType(db.rs.getByte(UserAuth.loginTypeName));
                userAuth.setIpAddress(db.rs.getString(UserAuth.ipAddressName));
                userAuth.setIpSource(db.rs.getString(UserAuth.ipSourceName));
                userAuth.setCreateTime(db.rs.getTimestamp(UserAuth.createTimeName));
                userAuth.setLastLoginTime(db.rs.getTimestamp(UserAuth.lastLoginTimeName));
                return userAuth;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return null;
    }

    /**
     * 根据userid查询
     * @param key
     * @return
     */
    public static UserAuth query(int key) {
        DBUtil db = new DBUtil();//数据库连接
        try {
            db.getConnection();
            String sql = "SELECT * FROM user_auth WHERE user_info_id=?";
            db.preStmt = db.conn.prepareStatement(sql);
            db.preStmt.setInt(1,key);

            db.rs = db.preStmt.executeQuery();

            while (db.rs.next()) {
                UserAuth userAuth = new UserAuth();
                userAuth.setId(db.rs.getInt(UserAuth.idName));
                userAuth.setUserInfoId(db.rs.getInt(UserAuth.userInfoIdName));
                userAuth.setUsername(db.rs.getString(UserAuth.usernameName));
                userAuth.setPassword(db.rs.getString(UserAuth.passwordName));
                userAuth.setLoginType(db.rs.getByte(UserAuth.loginTypeName));
                userAuth.setIpAddress(db.rs.getString(UserAuth.ipAddressName));
                userAuth.setIpSource(db.rs.getString(UserAuth.ipSourceName));
                userAuth.setCreateTime(db.rs.getTimestamp(UserAuth.createTimeName));
                userAuth.setLastLoginTime(db.rs.getTimestamp(UserAuth.lastLoginTimeName));
                return userAuth;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return null;
    }

}
