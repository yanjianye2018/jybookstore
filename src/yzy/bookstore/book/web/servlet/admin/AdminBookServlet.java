package yzy.bookstore.book.web.servlet.admin;

import cn.itcast.commons.CommonUtils;
import yzy.bookstore.book.domain.Book;
import yzy.bookstore.book.service.BookService;
import yzy.bookstore.category.domain.Category;
import yzy.bookstore.category.service.CategoryService;
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
@WebServlet(name = "AdminBookServlet", value = "/AdminBookServlet")
public class AdminBookServlet extends BaseServlet {
    private BookService bookService = new BookService();
    private CategoryService categoryService = new CategoryService();

    //查看 所有图书
    protected void findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("bookList", bookService.findAll());
        req.getRequestDispatcher("/adminjsps/admin/book/list.jsp").forward(req, resp);
    }

    //加载图书信息
    protected void load(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取图书bid
        String bid = req.getParameter("bid");
        //根据bid加载图书信息
        Book book = bookService.load(bid);
        //根据查询出所有分类信息
        List<Category> categoryList = categoryService.findAll();
        //将图书信息保存到request域中
        req.setAttribute("book", book);
        req.setAttribute("categoryList", categoryList);
        //转发到desc.jsp
        req.getRequestDispatcher("/adminjsps/admin/book/desc.jsp").forward(req, resp);
    }

    //查询所有分类
    protected void addPre(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("categoryList", categoryService.findAll());
        req.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(req, resp);
    }

    //删除图书
    protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取bid
        String bid = req.getParameter("bid");
        //根据bid删除图书
        bookService.delete(bid);
        findAll(req, resp);
    }

    //编辑功能
    protected void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Book book = CommonUtils.toBean(req.getParameterMap(), Book.class);
        Category category = CommonUtils.toBean(req.getParameterMap(), Category.class);
        //建立关联
        book.setCategory(category);
        bookService.edit(book);
        findAll(req, resp);

    }
}
