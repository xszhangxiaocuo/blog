package com.github.xszhangxiaocuo.dao;

import com.github.xszhangxiaocuo.entity.Err.ErrCode;
import com.github.xszhangxiaocuo.entity.sql.Article;
import com.github.xszhangxiaocuo.entity.sql.Category;
import com.github.xszhangxiaocuo.entity.sql.Tag;
import com.github.xszhangxiaocuo.utils.DBUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class ArticleDao {
    static Logger logger = Logger.getLogger(ArticleDao.class.getName());
    static DBUtil db = new DBUtil();//数据库连接

    public static int FINDALL = 0;//查找所有用户文章
    public static int FINDBYUSERID = 1;//查找一个用户的所有文章
    public static int FINDBYARTID = 2;//查找一篇文章

    /**
     * 传入Article对象进行插入，id自增
     * @param data
     * @return
     */
    public static ErrCode insert(Article data) {
        try {
            db.getConnection();

            String sql = "INSERT INTO article(user_id, category_id, article_cover, article_title, article_content,create_time, update_time,is_top,is_draft,is_delete) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?)";
            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setInt(1, data.getUserId());
            db.preStmt.setInt(2, data.getCategoryId());
            db.preStmt.setString(3, data.getArticleCover());
            db.preStmt.setString(4, data.getArticleTitle());
            db.preStmt.setString(5, data.getArticleContent());
            db.preStmt.setTimestamp(6, data.getCreateTime());
            db.preStmt.setTimestamp(7, data.getUpdateTime());
            db.preStmt.setByte(8, data.getIsTop());
            db.preStmt.setByte(9, data.getIsDraft());
            db.preStmt.setByte(10, data.getIsDelete());

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
            String sql = "update article set is_delete=1 where id = ?";//修改is_delete标记为删除
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
     * 直接传入Article对象进行更新
     * @param data
     * @return
     */
    public static ErrCode update(Article data) {
        try {
            db.getConnection();
            String sql = "UPDATE article SET " +
                    "user_id=?, category_id=?, article_cover=?, article_title=?, article_content=?,create_time=?, update_time=?,is_top=?,is_draft=?,is_delete=? " +
                    "WHERE id=?";
            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setInt(1, data.getUserId());
            db.preStmt.setInt(2, data.getCategoryId());
            db.preStmt.setString(3, data.getArticleCover());
            db.preStmt.setString(4, data.getArticleTitle());
            db.preStmt.setString(5, data.getArticleContent());
            db.preStmt.setTimestamp(6, data.getCreateTime());
            db.preStmt.setTimestamp(7, data.getUpdateTime());
            db.preStmt.setByte(8, data.getIsTop());
            db.preStmt.setByte(9, data.getIsDraft());
            db.preStmt.setByte(10, data.getIsDelete());

            db.preStmt.setInt(11,data.getId());

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
    public static List<Article> query(int key,int type) {
        List<Article> list = new ArrayList<>();
        try {
            db.getConnection();

            String sql = "SELECT * FROM article WHERE is_draft=0 AND is_delete=0";//查找所有未被删除且不是草稿的文章

            if (type==FINDBYUSERID){
                sql="SELECT * FROM article WHERE user_id=? AND is_draft=0 AND is_delete=0";
            }else if (type==FINDBYARTID){
                sql="SELECT * FROM article WHERE id=? AND is_draft=0 AND is_delete=0";
            }

            db.preStmt = db.conn.prepareStatement(sql);

            if (type!=FINDALL) {
                db.preStmt.setInt(1, key);
            }

            db.rs = db.preStmt.executeQuery();

            return getList(list);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return list;
    }

    /**
     * 查询所有文章或查询指定用户的文章
     * @param key
     * @param page 当前页数
     * @param pageSize 每一页的大小
     * @return
     */
    public static List<Article> query(int key,int type,int page,int pageSize) {
        List<Article> list = new ArrayList<>();
        try {
            db.getConnection();

            String sql = "SELECT * FROM article WHERE is_draft=0 AND is_delete=0 LIMIT ? OFFSET ?";//查找所有未被删除且不是草稿的文章

            if (type==FINDBYUSERID){
                sql="SELECT * FROM article WHERE user_id=? AND is_delete=0 AND is_draft=0 LIMIT ? OFFSET ?";
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

            return getList(list);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return list;
    }

    private static List<Article> getList(List<Article> list) throws SQLException {
        while (db.rs.next()) {
            Article article = new Article();
            article.setId(db.rs.getInt(Article.idName));
            article.setUserId(db.rs.getInt(Article.userIdName));
            article.setCategoryId(db.rs.getInt(Article.categoryIdName));
            article.setArticleCover(db.rs.getString(Article.articleCoverName));
            article.setArticleTitle(db.rs.getString(Article.articleTitleName));
            article.setArticleContent(db.rs.getString(Article.articleContentName));
            article.setCreateTime(db.rs.getTimestamp(Article.createTimeName));
            article.setUpdateTime(db.rs.getTimestamp(Article.updateTimeName));
            article.setIsTop(db.rs.getByte(Article.isTopName));
            article.setIsDraft(db.rs.getByte(Article.isDraftName));
            article.setIsDelete(db.rs.getByte(Article.isDeleteName));

            List<Tag> tags = ArticleTagDao.queryTag(article.getId());
            article.setTagList(tags);

            List<Category> category = CategoryDao.query(article.getCategoryId(),CategoryDao.FINDBYCATEGORYID);
            if (!category.isEmpty()) {
                article.setCategoryName(category.get(0).getCategoryName());
            }

            list.add(article);
        }
        return list;
    }

}
