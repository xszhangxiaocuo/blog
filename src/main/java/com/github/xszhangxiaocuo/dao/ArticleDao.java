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

    public static int FINDALL = 0;//查找所有用户文章
    public static int FINDBYUSERID = 1;//查找一个用户的所有文章
    public static int FINDBYARTID = 2;//查找一篇文章
    public static int FINDDELETE = 3;//查找被删除文章
    public static int FINDDRAFT = 4;//查找草稿文章
    public static int FINDPUBLISH = 5;//查找已发布文章

    /**
     * 传入Article对象进行插入，id自增
     * @param data
     * @return
     */
    public static ErrCode insert(Article data) {
        DBUtil db = new DBUtil();//数据库连接
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
        DBUtil db = new DBUtil();//数据库连接
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
        DBUtil db = new DBUtil();//数据库连接
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
    public static List<Article> query(int key,int type,byte isDelete,byte isDraft) {
        DBUtil db = new DBUtil();//数据库连接
        List<Article> list = new ArrayList<>();
        try {
            db.getConnection();

            String sql = "SELECT * FROM article WHERE is_draft=? AND is_delete=?";//查找所有未被删除且不是草稿的文章

            if (type==FINDBYUSERID){
                sql="SELECT * FROM article WHERE user_id=? AND is_draft=? AND is_delete=?";
            }else if (type==FINDBYARTID){
                sql="SELECT * FROM article WHERE id=? AND is_delete=?";
            }

            db.preStmt = db.conn.prepareStatement(sql);

            if (type==FINDALL) {
                db.preStmt.setByte(1,isDraft);
                db.preStmt.setByte(2,isDelete);
            }else if (type==FINDBYUSERID){
                db.preStmt.setInt(1, key);
                db.preStmt.setByte(2,isDraft);
                db.preStmt.setByte(3,isDelete);
            }else if (type==FINDBYARTID){
                db.preStmt.setInt(1,key);
                db.preStmt.setByte(2,isDelete);
            }

            db.rs = db.preStmt.executeQuery();
            list = getList(db,list);
            return list;
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
    public static List<Article> query(int key,int type,byte isDelete,byte isDraft,int page,int pageSize) {
        DBUtil db = new DBUtil();//数据库连接
        List<Article> list = new ArrayList<>();
        try {
            db.getConnection();

            String sql = "SELECT * FROM article WHERE is_draft=? AND is_delete=? LIMIT ? OFFSET ?";//查找所有未被删除且不是草稿的文章

            if (type==FINDBYUSERID){
                sql="SELECT * FROM article WHERE user_id=? AND is_delete=? AND is_draft=? LIMIT ? OFFSET ?";
            }

            db.preStmt = db.conn.prepareStatement(sql);
            if (type!=FINDALL) {
                db.preStmt.setInt(1, key);
                db.preStmt.setByte(2,isDraft);
                db.preStmt.setByte(3,isDelete);
                db.preStmt.setInt(4, pageSize);
                db.preStmt.setInt(5, (page - 1) * pageSize);
            }else {
                db.preStmt.setByte(1,isDraft);
                db.preStmt.setByte(2,isDelete);
                db.preStmt.setInt(3, pageSize);
                db.preStmt.setInt(4, (page - 1) * pageSize);

            }

            db.rs = db.preStmt.executeQuery();

            list = getList(db,list);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return list;
    }

    /**
     * 通过categoryid查找指定用户的文章
     * @param userId
     * @param categoryId
     * @param isDelete
     * @param isDraft
     * @return
     */
    public static List<Article> queryByCategory(int userId,int categoryId,byte isDelete,byte isDraft) {
        DBUtil db = new DBUtil();//数据库连接
        List<Article> list = new ArrayList<>();
        try {
            db.getConnection();

            String sql = "SELECT * FROM article WHERE user_id=? AND category_id=? AND is_draft=? AND is_delete=?";

            db.preStmt = db.conn.prepareStatement(sql);

                db.preStmt.setInt(1, userId);
                db.preStmt.setInt(2,categoryId);
                db.preStmt.setByte(3, isDraft);
                db.preStmt.setByte(4, isDelete);

            db.rs = db.preStmt.executeQuery();
            list = getList(db,list);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return list;
    }

    /**
     * 只根据isDelete进行查询
     * @param key
     * @param type
     * @param isDelete
     * @return
     */
    public static List<Article> queryByDelete(int key,int type,byte isDelete,int page,int pageSize) {
        DBUtil db = new DBUtil();//数据库连接
        List<Article> list = new ArrayList<>();
        try {
            db.getConnection();

            String sql = "SELECT * FROM article WHERE is_delete=? ";

            if (type==FINDBYUSERID){
                sql = "SELECT * FROM article WHERE user_id=? AND is_delete=? ";
            }
            if (page>0&&pageSize>0){
                sql+="LIMIT ? OFFSET ?";
            }
            db.preStmt = db.conn.prepareStatement(sql);
            if (type==FINDBYUSERID) {
                db.preStmt.setInt(1, key);
                db.preStmt.setInt(2,isDelete);
                if (page>0&&pageSize>0){
                    db.preStmt.setInt(3,pageSize);
                    db.preStmt.setInt(4,(page - 1) * pageSize);
                }
            }else {
                db.preStmt.setByte(1,isDelete);
                if (page>0&&pageSize>0){
                    db.preStmt.setInt(2,pageSize);
                    db.preStmt.setInt(3,(page - 1) * pageSize);
                }
            }

            db.rs = db.preStmt.executeQuery();
            list = getList(db,list);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return list;
    }

    /**
     * 只根据isDraft进行查询
     * @param key
     * @param type
     * @param isDraft
     * @return
     */
    public static List<Article> queryByDraft(int key,int type,byte isDraft,int page,int pageSize) {
        DBUtil db = new DBUtil();//数据库连接
        List<Article> list = new ArrayList<>();
        try {
            db.getConnection();

            String sql = "SELECT * FROM article WHERE is_draft=? AND is_delete=0 ";

            if (type==FINDBYUSERID){
                sql = "SELECT * FROM article WHERE user_id=? AND is_draft=? AND is_delete=0 ";
            }
            if (page>0&&pageSize>0){
                sql+="LIMIT ? OFFSET ?";
            }
            db.preStmt = db.conn.prepareStatement(sql);
            if (type==FINDBYUSERID) {
                db.preStmt.setInt(1, key);
                db.preStmt.setInt(2,isDraft);
                if (page>0&&pageSize>0){
                    db.preStmt.setInt(3,pageSize);
                    db.preStmt.setInt(4,(page - 1) * pageSize);
                }
            }else {
                db.preStmt.setByte(1,isDraft);
                if (page>0&&pageSize>0){
                    db.preStmt.setInt(2,pageSize);
                    db.preStmt.setInt(3,(page - 1) * pageSize);
                }
            }

            db.rs = db.preStmt.executeQuery();
            list = getList(db,list);
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return list;
    }

    public static Article queryById(int key) {
        DBUtil db = new DBUtil();//数据库连接
        List<Article> list = new ArrayList<>();
        try {
            db.getConnection();

            String sql = "SELECT * FROM article WHERE id=?";


            db.preStmt = db.conn.prepareStatement(sql);

            db.preStmt.setInt(1,key);

            db.rs = db.preStmt.executeQuery();
            list = getList(db,list);
            return list.get(0);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        return null;
    }

    private static List<Article> getList(DBUtil db,List<Article> list) throws SQLException {
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

            article.setNickname(UserInfoDao.query(article.getUserId()).getNickname());

            list.add(article);
        }
        return list;
    }

}
