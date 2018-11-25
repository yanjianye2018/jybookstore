package yzy.bookstore.order.domain;

import yzy.bookstore.user.domain.User;

import java.util.Date;
import java.util.List;

/**订单类
 * @author yzy
 */
public class Order {
    private String oid;//主键
    private Date ordertime;//下单时间
    private double total;//合计
    private int state;
    //订单状态4中 1未付款、2已付款未发货3已发货,未确认收货4已确认收货
    private User owner;//订单所有者,依赖uid,
    private String address;//收货地址

    private List<OrderItem> orderItemList;//当前订单下条目集合,关联orderItem

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Date getOrdertime() {
        return ordertime;
    }

    public void setOrdertime(Date ordertime) {
        this.ordertime = ordertime;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
