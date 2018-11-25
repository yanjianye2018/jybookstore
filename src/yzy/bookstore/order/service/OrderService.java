package yzy.bookstore.order.service;

import cn.itcast.jdbc.JdbcUtils;
import yzy.bookstore.order.dao.OrderDao;
import yzy.bookstore.order.domain.Order;

import java.sql.SQLException;
import java.util.List;

/**
 * @author yzy
 */
public class OrderService {
    private OrderDao orderDao = new OrderDao();

    public void add(Order order) {
        try {
            //开启事务
            JdbcUtils.beginTransaction();
            //插入订单
            orderDao.add(order);
            //插入订单条目
            orderDao.addOrderItemList(order.getOrderItemList());
            //提交事务
            JdbcUtils.commitTransaction();
        } catch (Exception e) {
            //回滚事务
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException e1) {
            }
            throw new RuntimeException(e);
        }
    }

    public List<Order> myOrders(String uid) {
        return orderDao.findByUid(uid);
    }

    public Order load(String oid) {
        return orderDao.load(oid);
    }

    public void confirm(String oid) throws OrderException {
        //先校验订单状态,获取订单状态
        int state = orderDao.getStateByOid(oid);
        if (state != 3) {
            throw new OrderException("订单确认失败");
        }

        //如果是修改订单状态为4
        orderDao.updateState(oid, 4);
    }
}
