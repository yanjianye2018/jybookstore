package yzy.bookstore.cart.domain;

import yzy.bookstore.book.domain.Book;

import java.math.BigDecimal;

/**购物车条目类
 * @author yzy
 */
public class CartItem {
    private Book book;//商品
    private int count;//数量

    public double getSubtotal() {//小计方法，但它没有对应的成员！
        BigDecimal d1 = new BigDecimal(book.getPrice() + "");
        BigDecimal d2 = new BigDecimal(count + "");
        return d1.multiply(d2).doubleValue();//小计=数量*单价
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
