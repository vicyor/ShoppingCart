package com.vicyor.application.service.ref;

import com.vicyor.application.po.ShoppingOrder;
import com.vicyor.application.po.ShoppingOrderSKU;
import com.vicyor.application.service.OrderService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 作者:姚克威
 * 时间:2020/4/6 0:15
 **/
@Service("orderServiceRef")
public class OrderServiceRef implements OrderService {
    @Reference OrderService ref;

    @Override
    public void handle(ShoppingOrder shoppingOrder, List dtos) {
        ref.handle(shoppingOrder,dtos);
    }

    @Override
    public void cancelOrderIfNecessary(String orderId, Long userId) throws Exception {
        ref.cancelOrderIfNecessary(orderId,userId);
    }

    @Override
    public List<ShoppingOrderSKU> getShoppingOrderSKU(String orderId) {
        return ref.getShoppingOrderSKU(orderId);
    }
}
