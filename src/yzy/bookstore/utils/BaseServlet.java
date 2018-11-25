package yzy.bookstore.utils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author yzy
 * @date 2017-11-03 - 11:20
 */
@WebServlet(name = "BaseServlet", value = "/BaseServlet")
public class BaseServlet extends HttpServlet {
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //处理编码问题
		req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        //规定传的参数名为method
        String methodName = req.getParameter("method");
        if (methodName == null || methodName.trim().isEmpty()) {
            throw new RuntimeException("您没有传递method参数,无法调用方法");
        }
        //获取当前类的字节码文件对象
        Class clazz = this.getClass();
        //获取方法对象
        Method method = null;
        try {
            //根据传递的方法名获取方法名对象
            method = clazz.getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("您要调用的方法" + methodName + "它不存在!");
        }
        method.setAccessible(true);
        //根据反射调用方法
        try {
            method.invoke(this, req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
