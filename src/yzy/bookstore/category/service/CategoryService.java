package yzy.bookstore.category.service;

import yzy.bookstore.book.dao.BookDao;
import yzy.bookstore.category.dao.CategoryDao;
import yzy.bookstore.category.domain.Category;

import java.util.List;

/**
 * @author yzy
 */
public class CategoryService {
    private CategoryDao categoryDao = new CategoryDao();
    private BookDao bookDao = new BookDao();

    public List<Category> findAll() {
        return categoryDao.findAll();
    }


    public void add(Category category) {
        categoryDao.add(category);
    }

    public void delete(String cid) throws CategoryException {
        //查询该分类下是否还有图书,如果有图书,不能删除
        int count = bookDao.getCountByCid(cid);
        if (count > 0) {
            throw new CategoryException("该分类下还有图书,不能删除!");
        }
        categoryDao.delete(cid);
    }

    public Category load(String cid) {
        return categoryDao.load(cid);
    }

    public void edit(Category category) {
        categoryDao.edit(category);
    }
}
