package yzy.bookstore.book.dao;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import yzy.bookstore.book.domain.Book;
import yzy.bookstore.category.domain.Category;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author yzy
 */
public class BookDao {
    private QueryRunner qr = new TxQueryRunner();

    //查看所有图书
    public List<Book> findAll() {
        try {
            String sql = "select * from book where del=false";
            return qr.query(sql, new BeanListHandler<Book>(Book.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> findByCategory(String cid) {
        //根据图书分类查询
        try {
            String sql = "select * from book where cid=? and del=false";
            return qr.query(sql, new BeanListHandler<Book>(Book.class), cid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Book findByBid(String bid) {
        try {
            //查询结果是单行
            String sql = "select * from book where bid=?";
            //map才能映射到分类的cid
            Map<String, Object> map = qr.query(sql, new MapHandler(), bid);
            Book book = CommonUtils.toBean(map, Book.class);
            Category category = CommonUtils.toBean(map, Category.class);
            book.setCategory(category);
            return book;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getCountByCid(String cid) {
        try {
            String sql = "select count(*) from book where cid=?";
            //结果是单行单列
            Number count = (Number) qr.query(sql, new ScalarHandler(), cid);
            return count.intValue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //添加图书
    public void add(Book book) {
        try {
            String sql = "insert into book values(?,?,?,?,?,?)";
            Object[] params = {book.getBid(), book.getBname(), book.getPrice(),
                    book.getAuthor(), book.getImage(), book.getCategory().getCid()};
            qr.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //删除图书
    public void delete(String bid) {
        try {
            String sql = "update book set del=true where bid=?";
            qr.update(sql, bid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void edit(Book book) {
        try {
            String sql = "update book set bname=?, price=?,author=?, image=?, cid=? where bid=?";
            Object[] params = {book.getBname(), book.getPrice(),
                    book.getAuthor(), book.getImage(),
                    book.getCategory().getCid(), book.getBid()};
            qr.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
