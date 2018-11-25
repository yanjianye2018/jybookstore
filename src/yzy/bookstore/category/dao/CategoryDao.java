package yzy.bookstore.category.dao;

import cn.itcast.jdbc.TxQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import yzy.bookstore.category.domain.Category;

import java.sql.SQLException;
import java.util.List;

/**
 * @author yzy
 */
public class CategoryDao {
    private QueryRunner qr = new TxQueryRunner();

    public List<Category> findAll() {
        try {
            String sql = "select * from category";
            return qr.query(sql, new BeanListHandler<Category>(Category.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void add(Category category) {
        try {
            String sql = "insert into category values(?,?)";
            qr.update(sql, category.getCid(), category.getCname());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String cid) {
        try {
            String sql = "delete from category where cid=?";
            qr.update(sql, cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Category load(String cid) {
        try {
            //先查再改
            String sql = "select * from category where cid=?";
            return qr.query(sql, new BeanHandler<Category>(Category.class), cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void edit(Category category) {
        try {
            String sql = "update category set cname=? where cid=?";
            qr.update(sql, category.getCname(), category.getCid());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
