package yzy.bookstore.user.web.filter;

import yzy.bookstore.user.domain.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author yzy
 */
@WebFilter(filterName = "LoginFilter", urlPatterns = {"/jsps/cart/*", "/jsps/order/*", "/CartServlet", "/OrderServlet"})
public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //从session中获取用户信息
        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("session_user");
        if (user != null) {
            chain.doFilter(req, resp);//放行
        } else {
            //保存错误信息到request域中
            req.setAttribute("msg", "您还没有登录!");
            //转发到login.jsp
            req.getRequestDispatcher("/jsps/user/login.jsp").forward(req, resp);
        }
    }

    public void init(FilterConfig config) throws ServletException {
    }
}
