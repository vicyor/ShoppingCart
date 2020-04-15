package com.vicyor.application.service.impl;

import com.vicyor.application.dto.SKUOrderDTO;
import com.vicyor.application.exception.InsufficientInventoryException;
import com.vicyor.application.exception.UpdateStockFailedException;
import com.vicyor.application.exception.VersionException;
import com.vicyor.application.po.ShoppingSKU;
import com.vicyor.application.service.LocalOrderService;
import com.vicyor.application.service.ShoppingSKUService;
import com.vicyor.application.util.ShoppingUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 作者:姚克威
 * 时间:2020/4/2 15:14
 **/
@Service
public class LocalOrderServiceImpl implements LocalOrderService {
    @Autowired
    @Qualifier("shoppingSKUServiceRef")
    private ShoppingSKUService shoppingSKUService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void verifySKUOrderDTOLists(List<SKUOrderDTO> dtos) throws Exception {
        for (SKUOrderDTO dto : dtos) {

            Long skuId = dto.getSkuId();
            //根据商品id获取商品
            ShoppingSKU sku = shoppingSKUService.getSKUBySKUId(skuId);
            //商品库存不足
            if (sku.getStock() - dto.getCount() < 0) {
                throw new InsufficientInventoryException(sku.getGoodsName());
            }
            //商品版本出现变更
            if (sku.getVersion() != dto.getVersion()) {
                throw new VersionException(sku.getGoodsName());
            }
        }
    }

    /**
     * 在rabbitmq发送端开启事务
     *
     * @param dtos
     * @throws Exception
     */
    @Transactional(transactionManager = "rabbitTransactionManager", rollbackFor = Exception.class)
    @Override
    public void executeCreateOrderTask(List<SKUOrderDTO> dtos) throws Exception {
        for (SKUOrderDTO dto : dtos) {
            Boolean updateResult = true;
            try {
                //减库存
                updateResult = shoppingSKUService.countDownSKUStock(dto.getSkuId(), dto.getCount());
                if (!updateResult) {
                    //减库存失败抛出异常回滚
                    throw new UpdateStockFailedException();
                }
            } catch (Exception e) {
                //SqlException 回滚
                throw e;
            }
        }

        //发送创建订单信息
        Map<String, Object> order = new HashMap<>();
        order.put("data", dtos);
        order.put("userId", ShoppingUtil.getShoppingUser().getId());
        order.put("orderId", UUID.randomUUID());
        rabbitTemplate.convertAndSend("order", "shopping.order.create", order);

        //发送清除购物车信息
        Map<String, Object> shoppingCart = new HashMap<>();
        shoppingCart.put("data", dtos);
        shoppingCart.put("userId", ShoppingUtil.getShoppingUser().getId());
        rabbitTemplate.convertAndSend("shopping-cart", "shopping.shoppingcart.empty", shoppingCart);
    }
}