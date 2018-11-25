package yzy.bookstore.user.dao;

import cn.itcast.jdbc.TxQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import yzy.bookstore.user.domain.User;

import java.sql.SQLException;

/**
 * 持久层
 *
 * @author yzy
 */
public class UserDao {
    private QueryRunner qr = new TxQueryRunner();

    //根据用户名查询出user
    public User findByUsername(String username) {
        try {
            String sql = "select * from tb_user where username=?";
            //查询出来的是单行结果集用BeanHanler
            return qr.query(sql, new BeanHandler<User>(User.class),
                    username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //根据email到数据库查询出user
    public User findByEmail(String email) {
        try {
            String sql = "select * from tb_user where email=?";
            //查询出来的是单行结果集用BeanHanler
            return qr.query(sql, new BeanHandler<User>(User.class),
                    email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //添加用户到数据库
    public void add(User form) {
        try {
            String sql = "insert into tb_user values(?,?,?,?,?,?)";
            Object[] params = {form.getUid(), form.getUsername(),
                    form.getPassword(), form.getEmail(), form.getCode(),
                    form.isState()};
            qr.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //根据激活码查询用户激活状态
    public User findByCode(String code) {
        try {
            String sql = "select * from tb_user where code=?";
            return qr.query(sql, new BeanHandler<User>(User.class), code);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //根据uid修改用户状态激活状态
    public void updateState(String uid, boolean state) {
        try {
            String sql = "update tb_user set state=? where uid=?";
            qr.update(sql, state, uid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
