package com.vicyor.application.service.ref;

import com.vicyor.application.po.ShoppingSKU;
import com.vicyor.application.service.ShoppingSKUService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

/**
 * 作者:姚克威
 * 时间:2020/3/24 19:29
 **/
@Service
public class ShoppingSKUServiceRef implements ShoppingSKUService {
    @Reference
   private ShoppingSKUService shoppingSKUService;

    @Override
    public Long getStockOfSKU(Long skuId) {
        return shoppingSKUService.getStockOfSKU(skuId);
    }

    @Override
    public ShoppingSKU getSKUBySKUId(Long skuId) {
        return shoppingSKUService.getSKUBySKUId(skuId);
    }
}
