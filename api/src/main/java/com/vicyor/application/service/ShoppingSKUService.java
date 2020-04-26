package com.vicyor.application.service;

import com.vicyor.application.po.ShoppingSKU;

import java.util.List;
import java.util.Map;

public interface ShoppingSKUService {
    //根据商品id获取商品的库存
    Long getStockOfSKU(Long skuId);
    //根据商品id获取商品
    ShoppingSKU getSKUBySKUId(Long skuId);

    Boolean countDownSKUStock(Long skuId, Long count);

    void restoreGoodsStock(String orderId, List  shoppingOrderSKUS) throws  Exception;

    Map<String, Object> getSkus(Integer from, Integer size);
}
