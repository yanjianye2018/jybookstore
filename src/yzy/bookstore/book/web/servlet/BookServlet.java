package yzy.bookstore.book.web.servlet;

import yzy.bookstore.book.domain.Book;
import yzy.bookstore.book.service.BookService;
import yzy.bookstore.utils.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author yzy
 */
@WebServlet(name = "BookServlet", value = "/BookServlet")
public class BookServlet extends BaseServlet {
    private BookService bookService = new BookService();

    //查看所有图书
    protected void findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Book> bookList = bookService.findAll();
        //将查询结果保存到request域中
        req.setAttribute("bookList", bookList);
        //转发到/jsps/book/list.jsp显示
        req.getRequestDispatcher("/jsps/book/list.jsp").forward(req, resp);
    }

    //按分类查询图书
    protected void findByCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取分类cid
        String cid = req.getParameter("cid");
        //使用cid查询图书分类
        List bookList = bookService.findByCategory(cid);
        //将查询结果保存到request域中
        req.setAttribute("bookList", bookList);
        //转发到/jsps/book/list.jsp中显示
        req.getRequestDispatcher("/jsps/book/list.jsp").forward(req, resp);
    }

    //加载图书
    protected void load(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //根据bid加载对应图书
        String bid = req.getParameter("bid");
        Book book = bookService.load(bid);
        //保存图书信息到request域中
        req.setAttribute("book", book);
        //转发到jsps/book/desc.jsp中显示
        req.getRequestDispatcher("/jsps/book/desc.jsp").forward(req, resp);
    }
}
