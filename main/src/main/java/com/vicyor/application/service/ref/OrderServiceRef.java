package com.vicyor.application.service.ref;

import com.vicyor.application.po.ShoppingOrder;
import com.vicyor.application.po.ShoppingOrderSKU;
import com.vicyor.application.service.OrderService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 作者:姚克威
 * 时间:2020/4/23 16:10
 **/
@Service("orderServiceRef")
public class OrderServiceRef implements OrderService {
    @Reference
    private OrderService orderService;

    @Override
    public void handle(ShoppingOrder shoppingOrder, List dtos) {
        orderService.handle(shoppingOrder, dtos);
    }

    @Override
    public void cancelOrderIfNecessary(String orderId, Long userId) throws Exception {
        orderService.cancelOrderIfNecessary(orderId, userId);
    }

    @Override
    public List<ShoppingOrderSKU> getShoppingOrderSKU(String orderId) {
        return orderService.getShoppingOrderSKU(orderId);
    }

    @Override
    public List<ShoppingOrder> listOrdersByUserId(Long userId) {
        return orderService.listOrdersByUserId(userId);
    }

    @Override
    public void deleteOrderByOrderId(String orderId) {
        orderService.deleteOrderByOrderId(orderId);
    }
}
