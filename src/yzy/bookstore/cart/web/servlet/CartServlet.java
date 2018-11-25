package yzy.bookstore.cart.web.servlet;

import yzy.bookstore.book.domain.Book;
import yzy.bookstore.book.service.BookService;
import yzy.bookstore.cart.domain.Cart;
import yzy.bookstore.cart.domain.CartItem;
import yzy.bookstore.utils.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author yzy
 */
@WebServlet(name = "CartServlet", value = "/CartServlet")
public class CartServlet extends BaseServlet {
    //添加购物车条目
    protected void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从session中获取登录时的购物车对象
        HttpSession session = req.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        //得到添加图书的bid
        String bid = req.getParameter("bid");
        //调用bookService通过bid查询到图书
        Book book = new BookService().load(bid);
        //得到添加的商品数量
        String _count = req.getParameter("count");
        int count = Integer.parseInt(_count);
        //将book和count存入条目中
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setCount(count);
        //将订单存入购物车中
        cart.add(cartItem);
        //转发到/jsps/cart/list.jsp
        req.getRequestDispatcher("/jsps/cart/list.jsp").forward(req, resp);
    }

    //清空购物车
    protected void clear(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //得到session
        HttpSession session = req.getSession();
        //得到车
        Cart cart = (Cart) session.getAttribute("cart");
        //清空车
        cart.clear();
        //转发到/jsps/cart/list.jsp
        req.getRequestDispatcher("/jsps/cart/list.jsp").forward(req, resp);
    }

    //删除购物车条目
    protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //得到session
        HttpSession session = req.getSession();
        //得到车
        Cart cart = (Cart) session.getAttribute("cart");
        //根据bid删除
        String bid = req.getParameter("bid");
        //删除指定条目
        cart.delete(bid);
        //转发到/jsps/cart/list.jsp
        req.getRequestDispatcher("/jsps/cart/list.jsp").forward(req, resp);
    }
}
