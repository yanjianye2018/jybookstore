package yzy.bookstore.category.web.servlet.admin;

import cn.itcast.commons.CommonUtils;
import yzy.bookstore.category.domain.Category;
import yzy.bookstore.category.service.CategoryException;
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
@WebServlet(name = "AdminCategoryServlet", value = "/AdminCategoryServlet")
public class AdminCategoryServlet extends BaseServlet {
    private CategoryService categoryService = new CategoryService();


    protected void findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //后台查询所有图书,调用service查询
        List<Category> categoryList = categoryService.findAll();
        //将查询结果保存到request域中
        req.setAttribute("categoryList", categoryList);
        //转发到list.jsp
        req.getRequestDispatcher("/adminjsps/admin/category/list.jsp").forward(req, resp);
    }


    protected void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //添加功能
        //封装表单数据
        Category category = CommonUtils.toBean(req.getParameterMap(), Category.class);
        //补全cid
        category.setCid(CommonUtils.uuid());
        //调用service完成添加
        categoryService.add(category);
        //返回查看所有页面
        findAll(req, resp);
    }


    protected void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取cid
        String cid = req.getParameter("cid");
        //根据cid删除分类
        try {
            categoryService.delete(cid);
            //返回查询页面
            findAll(req, resp);
        } catch (CategoryException e) {
            //保存错误信息到req域转发到msg.jsp显示错误信息
            req.setAttribute("msg", e.getMessage());
            req.getRequestDispatcher("/adminjsps/msg.jsp").forward(req, resp);
        }
    }


    protected void editPre(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //加载分类
        //获取cid
        String cid = req.getParameter("cid");
        Category category = categoryService.load(cid);
        //保存到request域中
        req.setAttribute("category", category);
        //转发到mod.jsp
        req.getRequestDispatcher("/adminjsps/admin/category/mod.jsp").forward(req, resp);
    }


    protected void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //修改分类
        Category category = CommonUtils.toBean(req.getParameterMap(), Category.class);
        categoryService.edit(category);
        findAll(req, resp);
    }
}
