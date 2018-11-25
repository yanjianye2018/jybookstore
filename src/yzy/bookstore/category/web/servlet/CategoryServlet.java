package yzy.bookstore.category.web.servlet;

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
@WebServlet(name = "CategoryServlet", value = "/CategoryServlet")
public class CategoryServlet extends BaseServlet {
    private CategoryService categoryService = new CategoryService();

    //查询所有分类
    protected void findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //service查询出所有分类
        List<Category> categoryList = categoryService.findAll();
        //将查询结果保存到request域中
        req.setAttribute("categoryList", categoryList);
        //转发/jsps/user/left.jsp
        req.getRequestDispatcher("/jsps/left.jsp").forward(req, resp);
    }
}
