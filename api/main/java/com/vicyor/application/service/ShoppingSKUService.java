package com.vicyor.application.service;

import com.vicyor.application.po.ShoppingSKU;

public interface ShoppingSKUService {
    //根据商品id获取商品的库存
    Long getStockOfSKU(Long skuId);
    //根据商品id获取商品
    ShoppingSKU getSKUBySKUId(Long skuId);

    Boolean countDownSKUStock(Long skuId, Long count);
}
