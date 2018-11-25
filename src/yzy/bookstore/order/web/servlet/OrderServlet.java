package yzy.bookstore.order.web.servlet;

import cn.itcast.commons.CommonUtils;
import yzy.bookstore.cart.domain.Cart;
import yzy.bookstore.cart.domain.CartItem;
import yzy.bookstore.order.domain.Order;
import yzy.bookstore.order.domain.OrderItem;
import yzy.bookstore.order.service.OrderException;
import yzy.bookstore.order.service.OrderService;
import yzy.bookstore.user.domain.User;
import yzy.bookstore.utils.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yzy
 */
@WebServlet(name = "OrderServlet", value = "/OrderServlet")
public class OrderServlet extends BaseServlet {
    private OrderService orderService = new OrderService();

    //添加订单,太绕
    protected void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从session中获取车
        HttpSession session = req.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        //向订单设置属性
        Order order = new Order();
        order.setOid(CommonUtils.uuid());
        order.setOrdertime(new Date());//下单时间
        order.setTotal(cart.getTotal());//合计
        order.setState(1);//未付款
        User user = (User) session.getAttribute("session_user");
        order.setOwner(user);//订单所有者

        //创建订单条目集合
        List<OrderItem> orderItemList = new ArrayList<>();
        //遍历购物车中所有条目
        for (CartItem cartItem : cart.getCartItems()) {
            //cart.getCartItems()是车中所有条目
            //往订单条目里加
            OrderItem orderItem = new OrderItem();
            orderItem.setIid(CommonUtils.uuid());
            orderItem.setBook(cartItem.getBook());
            orderItem.setSubtotal(cartItem.getSubtotal());
            orderItem.setCount(cartItem.getCount());
            orderItem.setOrder(order);//设置所属订单

            orderItemList.add(orderItem);//把订单条目添加到大集合中
        }

        //把所有订单条目添加到订单中
        order.setOrderItemList(orderItemList);
        //清空购物车
        cart.clear();
        //调用service添加订单
        orderService.add(order);
        //保存order到request域中
        req.setAttribute("order", order);
        //转发到/jsps/order/desc.jsp
        req.getRequestDispatcher("/jsps/order/desc.jsp").forward(req, resp);
    }

    //我的订单
    protected void myOrders(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从session中获取当前用户uid
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("session_user");
        String uid = user.getUid();
        //使用uid查询当前用户的所有订单
        List<Order> orderList = orderService.myOrders(uid);
        //把订单列表保存到request域中,转发到/jsps/order/list.jsp显示
        req.setAttribute("orderList", orderList);
        req.getRequestDispatcher("/jsps/order/list.jsp").forward(req, resp);
    }

    //加载订单,是单个订单！
    protected void load(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获得oid
        String oid = req.getParameter("oid");
        //得到订单对象
        Order order = orderService.load(oid);
        //将订单保存到request域中,转发到/jsps/order/desc.jsp
        req.setAttribute("order", order);
        req.getRequestDispatcher("/jsps/order/desc.jsp").forward(req, resp);
    }

    //确认收货
    protected void confirm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取当前用户的oid
        String oid = req.getParameter("oid");
        //调用service查询订单状态
        try {
            orderService.confirm(oid);
            req.setAttribute("msg", "恭喜您,交易成功!");
        } catch (OrderException e) {
            //保存失败信息到request域
            req.setAttribute("msg", e.getMessage());
        }
        //转发到msg.jsp
        req.getRequestDispatcher("/jsps/msg.jsp").forward(req, resp);
    }
}
