package com.github.xszhangxiaocuo.dao;

import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.sql.Article;
import com.github.xszhangxiaocuo.entity.sql.ArticleTag;
import com.github.xszhangxiaocuo.entity.sql.Tag;
import com.github.xszhangxiaocuo.utils.DBUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class ArticleTagDao {
    static Logger logger = Logger.getLogger(ArticleTagDao.class.getName());
    static DBUtil db = new DBUtil();//数据库连接

    public static int FINDALL = 0;//查找所有用户的标签
    public static int FINDBYARTID = 1;//查找一个文章的所有标签
    public static int FINDBYTAGID = 2;//通过标签查找有这个标签的所有文章

    /**
     * 传入Tag对象进行插入，id自增
     *
     * @param data
     * @return
     */
    public static ErrCode insert(ArticleTag data) {
        try {
            db.getConnection();

            String sql = "INSERT INTO article_tag(article_id,tag_id) " +
                    "VALUES(?,?)";
            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setInt(1, data.getArticleId());
            db.preStmt.setInt(2, data.getTagId());

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
    public static ErrCode delete(int key,int type) {
        try {
            db.getConnection();
            String sql = "DELETE FROM article_tag WHERE id = ?";

            if (type==FINDBYARTID){
                sql="DELETE FROM article_tag WHERE article_id = ?";
            }else if (type==FINDBYTAGID){
                sql="DELETE FROM article_tag WHERE tag_id = ?";
            }

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
     * 直接传入ArticleTag对象进行更新
     *
     * @param data
     * @return
     */
    public static ErrCode update(ArticleTag data) {
        try {
            db.getConnection();
            String sql = "UPDATE article_tag SET " +
                    "article_id=?,tag_id=? " +
                    "WHERE id=?";
            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setInt(1, data.getArticleId());
            db.preStmt.setInt(2, data.getTagId());
            db.preStmt.setInt(3, data.getId());

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
    public static List<ArticleTag> query(int key, int type) {
        List<ArticleTag> list = new ArrayList<>();
        try {
            db.getConnection();

            String sql = "SELECT * FROM article_tag ";

            if (type == FINDBYARTID) {
                sql = "SELECT * FROM article_tag WHERE article=? ";
            } else if (type == FINDBYTAGID) {
                sql = "SELECT * FROM article_tag WHERE tag_id=? ";
            }

            db.preStmt = db.conn.prepareStatement(sql);

            if (type != FINDALL) {
                db.preStmt.setInt(1, key);
            }

            db.rs = db.preStmt.executeQuery();

            return getTags(list);
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
    public static List<ArticleTag> query(int key, int type, int page, int pageSize) {
        List<ArticleTag> list = new ArrayList<>();
        try {
            db.getConnection();

            String sql = "SELECT * FROM article_tag LIMIT ? OFFSET ?";

            if (type == FINDBYARTID) {
                sql = "SELECT * FROM article_tag WHERE article_id=? LIMIT ? OFFSET ?";
            }else if (type==FINDBYTAGID){
                sql = "SELECT * FROM article_tag WHERE tag_id=? LIMIT ? OFFSET ?";
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

            return getTags(list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return list;
    }

    private static List<ArticleTag> getTags(List<ArticleTag> list) throws SQLException {
        while (db.rs.next()) {
            ArticleTag at = new ArticleTag();
            at.setId(db.rs.getInt(ArticleTag.tagIdName));
            at.setArticleId(db.rs.getInt(ArticleTag.articleIdName));
            at.setTagId(db.rs.getInt(ArticleTag.tagIdName));

            list.add(at);
        }
        return list;
    }

    /**
     * 查询一个文章的所有标签
     * @param key
     * @return
     */
    public static List<Tag> queryTag(int key) {
        List<Tag> list = new ArrayList<>();
        try {
            db.getConnection();

            String sql = "SELECT * FROM article_tag WHERE article_id=?";

            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setInt(1, key);

            db.rs = db.preStmt.executeQuery();

            while (db.rs.next()){
                List<Tag> tags = TagDao.query(db.rs.getInt(ArticleTag.tagIdName),TagDao.FINDBYTAGID);
                if (!tags.isEmpty()){
                    list.add(tags.get(0));
                }
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return list;
    }

    /**
     * 查询有该标签的所有文章
     * @param key
     * @return
     */
    public static List<Article> queryArticle(int key) {
        List<Article> list = new ArrayList<>();
        try {
            db.getConnection();

            String sql = "SELECT * FROM article_tag WHERE tag_id=?";

            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setInt(1, key);

            db.rs = db.preStmt.executeQuery();

            while (db.rs.next()){
                List<Article> articles = ArticleDao.query(db.rs.getInt(ArticleTag.articleIdName),ArticleDao.FINDBYARTID);
                if (!articles.isEmpty()){
                    list.add(articles.get(0));
                }
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return list;
    }
}


