package com.github.xszhangxiaocuo.dao;

import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.sql.Article;
import com.github.xszhangxiaocuo.entity.sql.Category;
import com.github.xszhangxiaocuo.utils.DBUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class CategoryDao {
    static Logger logger = Logger.getLogger(CategoryDao.class.getName());
    static DBUtil db = new DBUtil();//数据库连接

    public static int FINDALL = 0;//查找所有用户的分类
    public static int FINDBYUSERID = 1;//查找一个用户的所有分类
    public static int FINDBYCATEGORYID = 2;//查找一个分类

    /**
     * 传入Category对象进行插入，id自增
     * @param data
     * @return
     */
    public static ErrCode insert(Category data) {
        try {
            db.getConnection();

            String sql = "INSERT INTO category(category_name, create_time, user_id) " +
                    "VALUES(?,?,?)";
            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setString(1, data.getCategoryName());
            db.preStmt.setTimestamp(2, data.getCreateTime());
            db.preStmt.setInt(3, data.getUserId());

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
     * 根据id删除
     * @param key
     * @return
     */
    public static ErrCode delete(int key) {
        try {
            db.getConnection();
            String sql = "DELETE FROM category WHERE id = ?";
            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setInt(1,key);

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
     * 直接传入Category对象进行更新
     * @param data
     * @return
     */
    public static ErrCode update(Category data) {
        try {
            db.getConnection();
            String sql = "UPDATE category SET " +
                    "category_name=?, create_time=?, user_id=? " +
                    "WHERE id=?";
            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setString(1, data.getCategoryName());
            db.preStmt.setTimestamp(2, data.getCreateTime());
            db.preStmt.setInt(3, data.getUserId());
            db.preStmt.setInt(4,data.getId());

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
     * 支持不同类型的查询数据，没有返回null
     * @param key
     * @param type 查询类型
     * @return
     */
    public static List<Category> query(int key,int type) {
        List<Category> list = new ArrayList<>();
        try {
            db.getConnection();

            String sql = "SELECT * FROM category ";

            if (type==FINDBYUSERID){
                sql="SELECT * FROM category WHERE user_id=? ";
            }else if (type==FINDBYCATEGORYID){
                sql="SELECT * FROM category WHERE id=? ";
            }

            db.preStmt = db.conn.prepareStatement(sql);

            if (type!=FINDALL) {
                db.preStmt.setInt(1, key);
            }

            db.rs = db.preStmt.executeQuery();

            while (db.rs.next()) {
              Category category = new Category();
              category.setId(db.rs.getInt(Category.idName));
              category.setCategoryName(db.rs.getString(Category.categoryNameName));
              category.setCreateTime(db.rs.getTimestamp(Category.createTimeName));
              category.setUserId(db.rs.getInt(Category.userIdName));

              list.add(category);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return list;
    }

    /**
     * 查询所有分类或查询指定用户的分类
     * @param key
     * @param page 当前页数
     * @param pageSize 每一页的大小
     * @return
     */
    public static List<Category> query(int key,int type,int page,int pageSize) {
        List<Category> list = new ArrayList<>();
        try {
            db.getConnection();

            String sql = "SELECT * FROM category LIMIT ? OFFSET ?";

            if (type==FINDBYUSERID){
                sql="SELECT * FROM category WHERE user_id=? LIMIT ? OFFSET ?";
            }

            db.preStmt = db.conn.prepareStatement(sql);
            if (type!=FINDALL) {
                db.preStmt.setInt(1, key);
                db.preStmt.setInt(2, pageSize);
                db.preStmt.setInt(3, (page - 1) * pageSize);
            }else {
                db.preStmt.setInt(1, pageSize);
                db.preStmt.setInt(2, (page - 1) * pageSize);

            }

            db.rs = db.preStmt.executeQuery();

            while (db.rs.next()) {
                Category category = new Category();
                category.setId(db.rs.getInt(Category.idName));
                category.setCategoryName(db.rs.getString(Category.categoryNameName));
                category.setCreateTime(db.rs.getTimestamp(Category.createTimeName));
                category.setUserId(db.rs.getInt(Category.userIdName));

                list.add(category);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return list;
    }

}
