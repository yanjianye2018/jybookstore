package yzy.bookstore.book.web.servlet.admin;

import cn.itcast.commons.CommonUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import yzy.bookstore.book.domain.Book;
import yzy.bookstore.book.service.BookService;
import yzy.bookstore.category.domain.Category;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yzy
 */
@WebServlet(name = "AdminAddBookServlet", value = "/AdminAddBookServlet")
public class AdminAddBookServlet extends HttpServlet {
    private BookService bookService = new BookService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        //创建工厂
        DiskFileItemFactory factory = new DiskFileItemFactory();
        //得到解析器
        ServletFileUpload sfu = new ServletFileUpload(factory);
        //使用解析器解析request对象
        try {
            List<FileItem> fileItemList = sfu.parseRequest(request);
            Map<String, String> map = new HashMap<>();
            for (FileItem fileItem : fileItemList) {
                map.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
            }
            Book book = CommonUtils.toBean(map, Book.class);
            //设置bid
            book.setBid(CommonUtils.uuid());
            Category category = CommonUtils.toBean(map,Category.class);
            book.setCategory(category);
            //得到保存目录的真实路径
            String savepath = this.getServletContext().getRealPath("/book_img");
            System.out.println(savepath);
            //得到上传文件名称,
            String filename = CommonUtils.uuid() + "_" +
                    fileItemList.get(1).getName();//得到表单第二行的字段名称
            System.out.println(filename);
            //创建目标文件
            File destFile = new File(savepath, filename);
            //保存上传文件到目标文件位置
            fileItemList.get(1).write(destFile);
            //
            book.setImage("book/image" + filename);
            bookService.add(book);
            request.getRequestDispatcher("AdminBookServlet?method=findAll").
                    forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
