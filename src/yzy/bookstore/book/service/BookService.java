package yzy.bookstore.book.service;

import yzy.bookstore.book.dao.BookDao;
import yzy.bookstore.book.domain.Book;

import java.util.List;

/**
 * @author yzy
 */
public class BookService {
    private BookDao bookDao = new BookDao();

    public List<Book> findAll(){
        return bookDao.findAll();
    }

    public List<Book> findByCategory(String cid){
        return bookDao.findByCategory(cid);
    }

    public Book load(String bid) {
        return bookDao.findByBid(bid);
    }

    public void add(Book book) {
        bookDao.add(book);
    }

    public void delete(String bid){
        bookDao.delete(bid);
    }

    public void edit(Book book) {
        bookDao.edit(book);
    }
}
