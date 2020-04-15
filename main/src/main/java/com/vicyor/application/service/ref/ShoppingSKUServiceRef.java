package com.vicyor.application.service.ref;

import com.vicyor.application.po.ShoppingSKU;
import com.vicyor.application.service.ShoppingSKUService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 作者:姚克威
 * 时间:2020/4/5 13:42
 **/
@Service("shoppingSKUServiceRef")
public class ShoppingSKUServiceRef implements ShoppingSKUService {
    @Reference
    private ShoppingSKUService ref;

    @Override
    public Long getStockOfSKU(Long skuId) {
        return ref.getStockOfSKU(skuId);
    }

    @Override
    public ShoppingSKU getSKUBySKUId(Long skuId) {
        return ref.getSKUBySKUId(skuId);
    }

    @Override
    public Boolean countDownSKUStock(Long skuId, Long count) {
        return ref.countDownSKUStock(skuId, count);
    }

    @Override
    public void restoreGoodsStock(String orderId, List shoppingOrderSKUS) throws Exception {
        ref.restoreGoodsStock(orderId, shoppingOrderSKUS);
    }
}
