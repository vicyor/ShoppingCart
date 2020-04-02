package com.vicyor.application.service.impl;

import com.vicyor.application.po.ShoppingSKU;
import com.vicyor.application.repository.ShoppingSKURepository;
import com.vicyor.application.service.ShoppingSKUService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 作者:姚克威
 * 时间:2020/3/24 19:16
 **/
@Service
public class ShoppingSKUServiceImpl implements ShoppingSKUService {
    @Autowired
    ShoppingSKURepository repository;

    @Override
    public Long getStockOfSKU(Long skuId) {
        ShoppingSKU sku = repository.getOne(skuId);
        return sku.getStock();
    }

    @Override
    public ShoppingSKU getSKUBySKUId(Long skuId) {
        return repository.getOne(skuId);
    }

    @Override
    public Boolean countDownSKUStock(Long skuId, Long count) {
        int updateRows = repository.updateSKUStockRightly(skuId, count);
        return updateRows>0;
    }
}
