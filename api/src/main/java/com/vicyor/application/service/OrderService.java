package com.vicyor.application.service;

import com.vicyor.application.dto.SKUOrderDTO;
import com.vicyor.application.po.ShoppingOrder;
import com.vicyor.application.po.ShoppingOrderSKU;

import java.util.List;

public interface OrderService {
    void handle(ShoppingOrder shoppingOrder, List  dtos);

    void cancelOrderIfNecessary(String orderId,Long userId) throws Exception;

    List<ShoppingOrderSKU> getShoppingOrderSKU(String orderId);

    List<ShoppingOrder> listOrdersByUserId(Long userId);

    void deleteOrderByOrderId(String orderId);
}
