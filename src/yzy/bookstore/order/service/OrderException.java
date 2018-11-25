package yzy.bookstore.order.service;

/**
 * @author yzy
 */
public class OrderException extends Exception {
    public OrderException() {
    }

    public OrderException(String message) {
        super(message);
    }
}
