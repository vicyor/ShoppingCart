package com.vicyor.application.service;

import com.vicyor.application.dto.SKUOrderDTO;
import com.vicyor.application.po.ShoppingOrder;

import java.util.List;

public interface OrderService {
    void handle(ShoppingOrder shoppingOrder, List<SKUOrderDTO> dtos);

    void cancelOrderIfNecessary(String orderId);
}
