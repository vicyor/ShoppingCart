package com.vicyor.application.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vicyor.application.po.ShoppingOrderSKU;
import com.vicyor.application.po.ShoppingSKU;
import com.vicyor.application.repository.ShoppingSKURepository;
import com.vicyor.application.service.ShoppingSKUService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return updateRows > 0;
    }


    @Override
    @Transactional
    public void restoreGoodsStock(String orderId, List shoppingOrderSKUS) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        //对每一个商品进行恢复
        for (Object map : shoppingOrderSKUS) {
            String json = mapper.writeValueAsString(map);
            ShoppingOrderSKU sku = mapper.readValue(json, ShoppingOrderSKU.class);
            Long restoreCount = sku.getCount();
            Long skuId = sku.getShoppingSKU().getSkuId();
            System.err.println(skuId);
            repository.updateSKUStockRightly(skuId, restoreCount * -1);
        }
    }

    @Override
    public Map<String, Object> getSkus(Integer from, Integer size) {
        Page<ShoppingSKU> page = repository.findAll(PageRequest.of(from, size));
        Map<String,Object> skus=new HashMap<>();
        skus.put("skus",page.getContent());
        skus.put("total",page.getTotalElements());
        return skus;
    }

}
