package yzy.bookstore.order.domain;

import yzy.bookstore.book.domain.Book;

/**订单条目类
 * @author yzy
 */
public class OrderItem {
    private String iid;//主键
    private int count;//数量
    private double subtotal;//小计
    private Order order;//所属订单,对应表oid
    private Book book;//所要购买的图书,对应表bid

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
