package com.github.xszhangxiaocuo.dao;

import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.sql.Tag;
import com.github.xszhangxiaocuo.utils.DBUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class TagDao {
    static Logger logger = Logger.getLogger(TagDao.class.getName());

    public static int FINDALL = 0;//查找所有用户的标签
    public static int FINDBYUSERID = 1;//查找一个用户的所有标签
    public static int FINDBYTAGID = 2;//查找一个标签

    /**
     * 传入Tag对象进行插入，id自增
     *
     * @param data
     * @return
     */
    public static ErrCode insert(Tag data) {
        DBUtil db = new DBUtil();//数据库连接
        try {
            db.getConnection();

            String sql = "INSERT INTO tag(tag_name, create_time, user_id) " +
                    "VALUES(?,?,?)";
            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setString(1, data.getTagName());
            db.preStmt.setTimestamp(2, data.getCreateTime());
            db.preStmt.setInt(3, data.getUserId());

            int row = db.preStmt.executeUpdate();
            if (row > 0) {
                return ErrCode.OK;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return ErrCode.FAIL;
    }

    /**
     * 根据id删除
     *
     * @param key
     * @return
     */
    public static ErrCode delete(int key) {
        DBUtil db = new DBUtil();//数据库连接
        try {
            db.getConnection();
            String sql = "DELETE FROM tag WHERE id = ?";
            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setInt(1, key);

            int row = db.preStmt.executeUpdate();
            if (row > 0) {
                return ErrCode.OK;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return ErrCode.FAIL;
    }

    /**
     * 直接传入Tag对象进行更新
     *
     * @param data
     * @return
     */
    public static ErrCode update(Tag data) {
        DBUtil db = new DBUtil();//数据库连接
        try {
            db.getConnection();
            String sql = "UPDATE tag SET " +
                    "tag_name=?, create_time=?, user_id=? " +
                    "WHERE id=?";
            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setString(1, data.getTagName());
            db.preStmt.setTimestamp(2, data.getCreateTime());
            db.preStmt.setInt(3, data.getUserId());
            db.preStmt.setInt(4, data.getId());

            int row = db.preStmt.executeUpdate();
            if (row > 0) {
                return ErrCode.OK;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return ErrCode.FAIL;
    }

    /**
     * 支持不同类型的查询数据，没有返回null
     *
     * @param key
     * @param type 查询类型
     * @return
     */
    public static List<Tag> query(int key, int type) {
        DBUtil db = new DBUtil();//数据库连接
        List<Tag> list = new ArrayList<>();
        try {
            db.getConnection();

            String sql = "SELECT * FROM tag ";

            if (type == FINDBYUSERID) {
                sql = "SELECT * FROM tag WHERE user_id=? ";
            } else if (type == FINDBYTAGID) {
                sql = "SELECT * FROM tag WHERE id=? ";
            }

            db.preStmt = db.conn.prepareStatement(sql);

            if (type != FINDALL) {
                db.preStmt.setInt(1, key);
            }

            db.rs = db.preStmt.executeQuery();

            return getTags(db,list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return list;
    }

    /**
     * 查询所有标签或查询指定用户的标签
     *
     * @param key
     * @param page     当前页数
     * @param pageSize 每一页的大小
     * @return
     */
    public static List<Tag> query(int key, int type, int page, int pageSize) {
        DBUtil db = new DBUtil();//数据库连接
        List<Tag> list = new ArrayList<>();
        try {
            db.getConnection();

            String sql = "SELECT * FROM tag LIMIT ? OFFSET ?";

            if (type == FINDBYUSERID) {
                sql = "SELECT * FROM tag WHERE user_id=? LIMIT ? OFFSET ?";
            }

            db.preStmt = db.conn.prepareStatement(sql);
            if (type != FINDALL) {
                db.preStmt.setInt(1, key);
                db.preStmt.setInt(2, pageSize);
                db.preStmt.setInt(3, (page - 1) * pageSize);
            } else {
                db.preStmt.setInt(1, pageSize);
                db.preStmt.setInt(2, (page - 1) * pageSize);

            }

            db.rs = db.preStmt.executeQuery();

            return getTags(db,list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return list;
    }

    private static List<Tag> getTags(DBUtil db,List<Tag> list) throws SQLException {
        while (db.rs.next()) {
            Tag tag = new Tag();
            tag.setId(db.rs.getInt(tag.idName));
            tag.setTagName(db.rs.getString(tag.tagNameName));
            tag.setCreateTime(db.rs.getTimestamp(tag.createTimeName));
            tag.setUserId(db.rs.getInt(tag.userIdName));

            list.add(tag);
        }
        return list;
    }
}


