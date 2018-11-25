package yzy.bookstore.order.dao;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import yzy.bookstore.book.domain.Book;
import yzy.bookstore.order.domain.Order;
import yzy.bookstore.order.domain.OrderItem;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yzy
 */
public class OrderDao {
    private QueryRunner qr = new TxQueryRunner();

    //向数据库中插入订单
    public void add(Order order) {
        try {
            String sql = "insert into orders values(?,?,?,?,?,?)";
            //把util的Date转成sql
            Timestamp timestamp = new Timestamp(order.getOrdertime().getTime());
            Object[] params = {order.getOid(), timestamp,
                    order.getTotal(), order.getState(), order.getOwner().getUid(), order.getAddress()};
            qr.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //向数据库插入订单条目

    public void addOrderItemList(List<OrderItem> orderItemList) {
        try {
            String sql = "insert into orderitem values(?,?,?,?,?)";
            Object[][] params = new Object[orderItemList.size()][];
            //遍历orderItemList
            for (int i = 0; i < orderItemList.size(); i++) {
                //取出每条订单
                OrderItem orderItem = orderItemList.get(i);//返回每个i位置元素
                //给一维数组赋值
                params[i] = new Object[]{orderItem.getIid(), orderItem.getCount(),
                        orderItem.getSubtotal(), orderItem.getOrder().getOid(),
                        orderItem.getBook().getBid()};
            }
            //执行批处理
            qr.batch(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<Order> findByUid(String uid) {
        try {
            //得到当前用户所有订单
            String sql = "select * from orders where uid=?";
            List<Order> orderList = qr.query(sql,
                    new BeanListHandler<Order>(Order.class), uid);
            //循环遍历每个订单,加载所有订单条目
            for (Order order : orderList) {
                loadOrderItems(order);//为order对象加载所有订单条目
            }
            return orderList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadOrderItems(Order order) throws SQLException {//这边也绕
        //加载指定订单的所有订单条目,多表查询,单表没有图书信息,单价之类的
        String sql = "select * from " +
                "orderItem i, book b where i.bid=b.bid and oid=?";
        List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), order.getOid());
        //每个map对应一行结果集?
        //一行:{iid=C7AD5492F27D492189105FB50E55CBB6, count=2,
        // subtotal=60.0, oid=1AE8A70354C947F8B81B80ADA6783155,
        // bid=7, bname=xxx,price=30.0, author=xxx,
        // image=book_img/8991366-1_l.jpg, cid=2}...
        List<OrderItem> orderItemList = toOrderItemList(mapList);
        order.setOrderItemList(orderItemList);
    }

    private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
        //把mapList每个map转换成两个对象
        List<OrderItem> orderItemList = new ArrayList<>();
        //循环遍历map
        for (Map<String, Object> map : mapList) {
            //把一个map转换成一个orderItem
            OrderItem orderItem = toOrderItem(map);
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    private OrderItem toOrderItem(Map<String, Object> map) {
        OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
        Book book = CommonUtils.toBean(map, Book.class);
        //建立二者关系
        orderItem.setBook(book);
        return orderItem;
    }

    public Order load(String oid) {
        try {
            String sql = "select * from orders where oid=?";
            //单行的
            Order order = qr.query(sql, new BeanHandler<Order>(Order.class), oid);
            loadOrderItems(order);
            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //通过oid查询当前用户订单状态
    public int getStateByOid(String oid) {
        try {
            String sql = "select state from orders where oid=?";
            //结果是单行单列
            return (Integer) qr.query(sql, new ScalarHandler(), oid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //修改订单状态
    public void updateState(String oid, int state) {
        try {
            String sql = "update orders set state=? where oid=?";
            qr.update(sql, state, oid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}