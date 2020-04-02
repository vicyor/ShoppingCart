package com.vicyor.application.service;

import com.vicyor.application.dto.SKUOrderDTO;
import com.vicyor.application.po.ShoppingOrder;
import com.vicyor.application.po.ShoppingOrderSKU;
import com.vicyor.application.repository.ShoppingOrderRepository;
import com.vicyor.application.repository.ShoppingOrderSKURepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 作者:姚克威
 * 时间:2020/4/2 22:19
 **/
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ShoppingOrderRepository shoppingOrderRepository;
    @Autowired
    private ShoppingOrderSKURepository shoppingOrderSKURepository;

    @Override
    @Transactional(transactionManager = "jpaTransactionManager")
    public void handle(ShoppingOrder shoppingOrder, List<SKUOrderDTO> dtos) {
        //将shopping_order数据持久化
        shoppingOrderRepository.save(shoppingOrder);
        for (SKUOrderDTO dto : dtos) {
            //将ShoppingOrderSKU数据持久化
            ShoppingOrderSKU sku = new ShoppingOrderSKU(shoppingOrder.getId(), dto.getSkuId(), dto.getCount());
            shoppingOrderSKURepository.save(sku);
        }
    }

}
