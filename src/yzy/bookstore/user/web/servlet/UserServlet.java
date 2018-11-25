package yzy.bookstore.user.web.servlet;

import cn.itcast.commons.CommonUtils;

import yzy.bookstore.cart.domain.Cart;
import yzy.bookstore.user.domain.User;
import yzy.bookstore.user.service.UserException;
import yzy.bookstore.user.service.UserService;
import yzy.bookstore.utils.BaseServlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * web层
 *
 * @author yzy
 */
@WebServlet(name = "UserServlet", value = "/UserServlet")
public class UserServlet extends BaseServlet {
    private UserService userService = new UserService();

    //退出功能
    protected void quit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //销毁session
        req.getSession().invalidate();
        //重定向到主页index.jsp
        resp.sendRedirect("/index.jsp");
    }

    //登录功能
    protected void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取登录表单的数据
        User form = CommonUtils.toBean(req.getParameterMap(), User.class);
        try {
            //校验出来的是单个对象
            User user = userService.login(form);
            //登录成功,保存登录信息到session中
            HttpSession session = req.getSession();
            session.setAttribute("session_user", user);
            //给用户添加购物车,向session中保存cart对象
            session.setAttribute("cart",new Cart());
            //重定向到主页
            resp.sendRedirect("/index.jsp");
        } catch (UserException e) {
            //如果校验失败,保存错误信息到request域中
            req.setAttribute("msg", e.getMessage());
            //保存form到request域中,回显
            req.setAttribute("form", form);
            //转发到login.jsp
            req.getRequestDispatcher("/jsps/user/login.jsp").forward(req, resp);
        }
    }

    //激活功能
    protected void active(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取激活码参数
        String code = req.getParameter("code");
        //通过激活码查询用户激活状态
        try {
            userService.active(code);
            //校验成功保存成功信息到request域中
            req.setAttribute("msg", "恭喜您,激活成功,请马上登录!");
        } catch (UserException e) {
            //如果激活校验失败,保存错误信息到request域中
            req.setAttribute("msg", e.getMessage());
        }
        //转发到msg.jsp
        req.getRequestDispatcher("/jsps/msg.jsp");
    }

    protected void regist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取表单传过来的参数
        Map<String, String[]> map = req.getParameterMap();
        //一键封装表单数据到User form对象中,此时表单数据已经在对象里了
        User form = CommonUtils.toBean(map, User.class);
        //补全表单内没有的项,uid和激活码
        form.setUid(CommonUtils.uuid());
        form.setCode(CommonUtils.uuid());
        //创建一个Map，用来封装错误信息，
        // 其中key为表单字段名称，值为错误信息
        Map<String, String> errors = new HashMap<>();

        //校验用户名长度是否合法
        String username = form.getUsername();
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "用户名不能为空！");
        } else if (username.length() < 3 || username.length() > 10) {
            errors.put("username", "用户名长度必须在3~10位之间！");
        }
        //校验密码长度是否合法
        String password = form.getPassword();
        if (password == null || password.trim().isEmpty()) {
            errors.put("password", "密码不能为空！");
        } else if (password.length() < 3 || password.length() > 10) {
            errors.put("password", "密码长度必须在3~10位之间！");
        }
        //校验邮箱是否合法
        String email = form.getEmail();
        if (email == null || email.trim().isEmpty()) {
            errors.put("email", "Email不能为空！");
        } else if (!email.matches("\\w+@\\w+\\.\\w+")) {
            errors.put("email", "Email格式错误！");
        }
        //如果校验不通过,显示错误信息
        if (errors.size() > 0) {
            //保存错误信息到request域中
            req.setAttribute("errors", errors);
            //保存form到request域中,用来回显
            req.setAttribute("form", form);
            //转发到regist.jsp中显示
            req.getRequestDispatcher("/jsps/user/regist.jsp").forward(req, resp);
            //程序不往下执行
            return;
        }

        //传递form到service层校验,
        try {
            userService.regist(form);
        } catch (UserException e) {
            //如果校验失败,继续把错误信息保存到request域中
            req.setAttribute("msg", e.getMessage());
            //保存form到request域用来回显
            req.setAttribute("form", form);
            //转发到msg.jsp
            req.getRequestDispatcher("/jsps/msg.jsp").forward(req, resp);
        }
        //发邮件,略！
//        try {
//            MailUtils.sendMail("xxx",form.getCode());
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
        //如果校验没问题,保存成功信息到request域中
        req.setAttribute("msg", "恭喜您,注册成功!" +
                "请马上到邮箱激活！");//成功信息嘛,当然不会有异常,不要e.getMessage()
        //转发到msg.jsp
        req.getRequestDispatcher("jsps/msg.jsp").forward(req, resp);
    }


}
