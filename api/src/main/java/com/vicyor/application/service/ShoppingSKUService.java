package com.vicyor.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vicyor.application.po.ShoppingSKU;

import java.util.List;

public interface ShoppingSKUService {
    //根据商品id获取商品的库存
    Long getStockOfSKU(Long skuId);
    //根据商品id获取商品
    ShoppingSKU getSKUBySKUId(Long skuId);

    Boolean countDownSKUStock(Long skuId, Long count);

    void restoreGoodsStock(String orderId, List  shoppingOrderSKUS) throws  Exception;
}
