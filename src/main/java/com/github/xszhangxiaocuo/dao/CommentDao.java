package com.github.xszhangxiaocuo.dao;

import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.sql.Article;
import com.github.xszhangxiaocuo.entity.sql.Comment;
import com.github.xszhangxiaocuo.entity.sql.UserInfo;
import com.github.xszhangxiaocuo.utils.DBUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class CommentDao {
    static Logger logger = Logger.getLogger(CommentDao.class.getName());

    public static int FINDALL = 0;//查找所有用户的标签
    public static int FINDBYARTID = 1;//查找一个文章的所有评论评论
    public static int FINDBYUSERID = 2;//查找一个用户的所有标签
    public static int FINDBYCOMMENTID=3;//查找指定评论


    /**
     * 传入Comment对象进行插入，id自增
     *
     * @param data
     * @return
     */
    public static ErrCode insert(Comment data) {
        DBUtil db = new DBUtil();//数据库连接
        try {
            db.getConnection();

            String sql = "INSERT INTO comment(user_id,article_id,comment_content, create_time, is_delete) " +
                    "VALUES(?,?,?,?,?)";
            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setInt(1, data.getUserId());
            db.preStmt.setInt(2, data.getArrticleId());
            db.preStmt.setString(3, data.getCommentContent());
            db.preStmt.setTimestamp(4,data.getCreateTime());
            db.preStmt.setByte(5,data.getIsDelete());

            int row = db.preStmt.executeUpdate();
            if (row > 0) {
                return ErrCode.OK;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return ErrCode.ERROR_DB_OPE;
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
            String sql = "UPDATE comment SET is_delete=1 WHERE id = ?";
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
        return ErrCode.ERROR_DB_OPE;
    }

    /**
     * 直接传入Comment对象进行更新
     *
     * @param data
     * @return
     */
    public static ErrCode update(Comment data) {
        DBUtil db = new DBUtil();//数据库连接
        try {
            db.getConnection();
            String sql = "UPDATE comment SET " +
                    "user_id=?,article_id=?,comment_content=?, create_time=?, is_delete=? " +
                    "WHERE id=?";
            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setInt(1, data.getUserId());
            db.preStmt.setInt(2, data.getArrticleId());
            db.preStmt.setString(3, data.getCommentContent());
            db.preStmt.setTimestamp(4,data.getCreateTime());
            db.preStmt.setByte(5,data.getIsDelete());

            db.preStmt.setInt(6,data.getId());

            int row = db.preStmt.executeUpdate();
            if (row > 0) {
                return ErrCode.OK;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return ErrCode.ERROR_DB_OPE;
    }

    public static List<Comment> query(int key, int type,byte isDelete) {
        DBUtil db = new DBUtil();//数据库连接
        List<Comment> list = new ArrayList<>();
        try {
            db.getConnection();

            String sql = "SELECT * FROM comment WHERE is_delete=? ";

            if (type == FINDBYUSERID) {
                sql = "SELECT * FROM comment WHERE user_id=? AND is_delete=? ";
            } else if (type == FINDBYARTID) {
                sql = "SELECT * FROM comment WHERE article_id=? AND is_delete=? ";
            }else if (type==FINDBYCOMMENTID){
                sql = "SELECT * FROM comment WHERE id=? AND is_delete=? ";
            }

            db.preStmt = db.conn.prepareStatement(sql);

            if (type != FINDALL) {
                db.preStmt.setInt(1, key);
                db.preStmt.setByte(2,isDelete);
            }else {
                db.preStmt.setByte(1,isDelete);
            }

            db.rs = db.preStmt.executeQuery();

            return getComments(db,list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return list;
    }

    /**
     * 查询所有评论和指定文章，指定用户的评论
     * 分页查询
     * @param key
     * @param type 查询类型
     * @return
     */
    public static List<Comment> query(int key, int type,byte isDelete,int page,int pageSize) {
        DBUtil db = new DBUtil();//数据库连接
        List<Comment> list = new ArrayList<>();
        try {
            db.getConnection();

            String sql = "SELECT * FROM comment WHERE is_delete=? LIMIT ? OFFSET ? ";

            if (type == FINDBYUSERID) {
                sql = "SELECT * FROM comment WHERE user_id=? AND is_delete=? LIMIT ? OFFSET ?";
            } else if (type == FINDBYARTID) {
                sql = "SELECT * FROM comment WHERE article_id=? AND is_delete=? LIMIT ? OFFSET ? ";
            }

            db.preStmt = db.conn.prepareStatement(sql);

            if (type != FINDALL) {
                db.preStmt.setInt(1, key);
                db.preStmt.setByte(2,isDelete);
                db.preStmt.setInt(3, pageSize);
                db.preStmt.setInt(4, (page - 1) * pageSize);
            }else {
                db.preStmt.setByte(1,isDelete);
                db.preStmt.setInt(2, pageSize);
                db.preStmt.setInt(3, (page - 1) * pageSize);
            }

            db.rs = db.preStmt.executeQuery();

            return getComments(db,list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return list;
    }


    private static List<Comment> getComments(DBUtil db,List<Comment> list) throws SQLException {
        while (db.rs.next()) {
            Comment comment = new Comment();
            comment.setId(db.rs.getInt(Comment.idName));
            comment.setUserId(db.rs.getInt(Comment.userIdName));
            comment.setArrticleId(db.rs.getInt(Comment.arrticleIdName));
            comment.setCommentContent(db.rs.getString(Comment.commentContentName));
            comment.setCreateTime(db.rs.getTimestamp(Comment.createTimeName));
            comment.setIsDelete(db.rs.getByte(Comment.isDeleteName));

            UserInfo userInfo = null;
            userInfo = UserInfoDao.query(comment.getUserId());
            if (userInfo!=null){
                comment.setAvatar(userInfo.getAvatar());
                comment.setNickname(userInfo.getNickname());
            }
            Article article = ArticleDao.query(comment.getArrticleId(),ArticleDao.FINDBYARTID,(byte)0,(byte) 0).get(0);
            comment.setArticleTitle(article.getArticleTitle());

            list.add(comment);
        }
        return list;
    }
}


