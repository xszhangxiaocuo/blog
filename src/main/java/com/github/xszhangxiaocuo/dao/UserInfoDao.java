package com.github.xszhangxiaocuo.dao;

import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.sql.UserInfo;
import com.github.xszhangxiaocuo.entity.sql.UserInfo;
import com.github.xszhangxiaocuo.utils.DBUtil;

import java.math.BigInteger;
import java.util.logging.Logger;


public class UserInfoDao {
    static Logger logger = Logger.getLogger(UserInfoDao.class.getName());
    static DBUtil db = new DBUtil();//数据库连接

    /**
     * 传入UserInfo对象进行插入，id自增
     * @param data
     * @return
     */
    public static ErrCode insert(UserInfo data) {
        try {
            //用户已经存在
            if (query(data.getNickname())!=null){
                return ErrCode.ERROR_USER_NAME_USED;
            }
            db.getConnection();

            String sql = "INSERT INTO user_info(user_role, nickname, avatar, intro, web_site,create_time, is_silence,blog_title) " +
                    "VALUES(?,?,?,?,?,?,?,?)";
            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setString(1, data.getUserRole());
            db.preStmt.setString(2, data.getNickname());
            db.preStmt.setString(3, data.getAvatar());
            db.preStmt.setString(4, data.getIntro());
            db.preStmt.setString(5, data.getWebsite());
            db.preStmt.setTimestamp(6, data.getCreateTime());
            db.preStmt.setByte(7, data.getIsSilence());
            db.preStmt.setString(8,data.getBlogTitle());

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
     * 根据nickname删除
     * @param key
     * @return
     */
    public static ErrCode delete(String key) {
        try {
            db.getConnection();
            String sql = "delete from user_info where nickname = ?";
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
     * 直接传入UserInfo对象进行更新
     * @param data
     * @return
     */
    public static ErrCode update(UserInfo data) {
        try {
            db.getConnection();
            String sql = "UPDATE user_info SET " +
                    "user_role=?, nickname=?, avatar=?, intro=?, web_site=?,create_time=?, is_silence=?,blog_title=? " +
                    "WHERE nickname=?";
            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setString(1,data.getUserRole());
            db.preStmt.setString(2, data.getNickname());
            db.preStmt.setString(3, data.getAvatar());
            db.preStmt.setString(4, data.getIntro());
            db.preStmt.setString(5, data.getWebsite());
            db.preStmt.setTimestamp(6, data.getCreateTime());
            db.preStmt.setByte(7, data.getIsSilence());
            db.preStmt.setString(8,data.getBlogTitle());

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
     * 根据nickname查询数据，没有返回null
     * @param key
     * @return
     */
    public static UserInfo query(String key) {
        try {
            db.getConnection();
            String sql = "SELECT * FROM user_info WHERE nickname=?";
            db.preStmt = db.conn.prepareStatement(sql);
            db.preStmt.setString(1,key);

            db.rs = db.preStmt.executeQuery();

            while (db.rs.next()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setId(db.rs.getInt(UserInfo.idName));
                userInfo.setUserRole(db.rs.getString(UserInfo.userRoleName));
                userInfo.setNickname(db.rs.getString(UserInfo.nicknameName));
                userInfo.setAvatar(db.rs.getString(UserInfo.avatarName));
                userInfo.setIntro(db.rs.getString(UserInfo.introName));
                userInfo.setWebsite(db.rs.getString(UserInfo.websiteName));
                userInfo.setCreateTime(db.rs.getTimestamp(UserInfo.createTimeName));
                userInfo.setIsSilence(db.rs.getByte(UserInfo.isSilenceName));
                userInfo.setBlogTitle(db.rs.getString(UserInfo.blogTitleName));

                return userInfo;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return null;
    }

    /**
     * 根据id查询数据，没有返回null
     * @param key
     * @return
     */
    public static UserInfo query(int key) {
        try {
            db.getConnection();
            String sql = "SELECT * FROM user_info WHERE id=?";
            db.preStmt = db.conn.prepareStatement(sql);
            db.preStmt.setInt(1,key);

            db.rs = db.preStmt.executeQuery();

            while (db.rs.next()) {
                UserInfo userInfo = new UserInfo();
                userInfo.setId(db.rs.getInt(UserInfo.idName));
                userInfo.setUserRole(db.rs.getString(UserInfo.userRoleName));
                userInfo.setNickname(db.rs.getString(UserInfo.nicknameName));
                userInfo.setAvatar(db.rs.getString(UserInfo.avatarName));
                userInfo.setIntro(db.rs.getString(UserInfo.introName));
                userInfo.setWebsite(db.rs.getString(UserInfo.websiteName));
                userInfo.setCreateTime(db.rs.getTimestamp(UserInfo.createTimeName));
                userInfo.setIsSilence(db.rs.getByte(UserInfo.isSilenceName));
                userInfo.setBlogTitle(db.rs.getString(UserInfo.blogTitleName));

                return userInfo;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return null;
    }

}
