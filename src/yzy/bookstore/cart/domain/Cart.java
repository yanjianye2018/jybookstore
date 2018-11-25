package yzy.bookstore.cart.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**购物车
 * @author yzy
 */
public class Cart {
    private Map<String, CartItem> map = new LinkedHashMap<>();

    //清空所有条目
    public void clear() {
        map.clear();
    }

    //删除指定条目
    public void delete(String bid) {
        map.remove(bid);//根据键删除订单条目
    }

    //获取所有条目
    public Collection<CartItem> getCartItems() {
        return map.values();//返回条目,CartItem
    }

    //添加条目到购物车中
    public void add(CartItem cartItem) {
        //判断购物车中是否存在原条目
        if (map.containsKey(cartItem.getBook().getBid())) {
            //先得到就条目数量
            CartItem oldCartItem = map.get(cartItem.getBook().getBid());//返回旧条目
            //旧条目数量
            int oldCount = oldCartItem.getCount();
            //新条目数量
            int newCount = cartItem.getCount();
            //更新老条目数量
            cartItem.setCount(oldCount + newCount);
            //装回map
            map.put(cartItem.getBook().getBid(), cartItem);
        } else {
            //没有就直接添加条目
            map.put(cartItem.getBook().getBid(), cartItem);
        }
        //这个不行,map集合会覆盖好像,测试过添加重复的还是显示1个
       // map.put(cartItem.getBook().getBid(), cartItem);

    }
    //计算合计
    public double getTotal() {
        // 合计=所有条目的小计之和
        BigDecimal total = new BigDecimal("0");
        for(CartItem cartItem : map.values()) {
            BigDecimal subtotal = new BigDecimal("" + cartItem.getSubtotal());
            total = total.add(subtotal);
        }
        return total.doubleValue();
    }
}
