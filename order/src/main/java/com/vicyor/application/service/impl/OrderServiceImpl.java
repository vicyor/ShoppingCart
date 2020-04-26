package com.vicyor.application.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vicyor.application.dto.SKUOrderDTO;
import com.vicyor.application.po.ShoppingOrder;
import com.vicyor.application.po.ShoppingOrderSKU;
import com.vicyor.application.po.ShoppingSKU;
import com.vicyor.application.repository.ShoppingOrderRepository;
import com.vicyor.application.repository.ShoppingOrderSKURepository;
import com.vicyor.application.service.OrderService;
import com.vicyor.application.service.ShoppingSKUService;
import com.vicyor.application.service.UserShoppingCartService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    @Qualifier("shoppingSKUServiceRef")
    @Autowired
    private ShoppingSKUService shoppingSKUService;
    @Qualifier("userShoppingCartServiceRef")
    @Autowired
    private UserShoppingCartService userShoppingCartService;

    @Override
    @Transactional(transactionManager = "jpaTransactionManager")
    public void handle(ShoppingOrder shoppingOrder, List dtos) {
        //将shopping_order数据持久化
        shoppingOrderRepository.save(shoppingOrder);
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (Object obj : dtos) {
                String json = mapper.writeValueAsString(obj);
                SKUOrderDTO dto = mapper.readValue(json, SKUOrderDTO.class);
                //将ShoppingOrderSKU数据持久化
                ShoppingOrderSKU sku = new ShoppingOrderSKU(shoppingOrder.getId(), new ShoppingSKU(dto.getSkuId()), dto.getCount());
                shoppingOrderSKURepository.save(sku);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    @Transactional
    public void cancelOrderIfNecessary(String orderId, Long userId) throws Exception {
        //根据订单Id获取订单示例
        ShoppingOrder order = shoppingOrderRepository.getOne(orderId);
        if (order.getStatus().equals(0)) {
            //更新订单状态为已取消
            shoppingOrderRepository.updateOrderStatus(orderId, 2);
            List<ShoppingOrderSKU> shoppingOrderSKUS = shoppingOrderSKURepository.findAllByOrderIdEquals(orderId);
            //恢复商品的库存
            shoppingSKUService.restoreGoodsStock(orderId, shoppingOrderSKUS);
            //恢复用户购物车的库存
            userShoppingCartService.restoreUserShoppingCart(userId, shoppingOrderSKUS);
        }
    }

    @Override
    public List<ShoppingOrderSKU> getShoppingOrderSKU(String orderId) {
        List<ShoppingOrderSKU> orderSKUList = shoppingOrderSKURepository.findAllByOrderIdEquals(orderId);
        return orderSKUList;
    }

    @Override
    public List<ShoppingOrder> listOrdersByUserId(Long userId) {
        return shoppingOrderRepository.findAllByUserIdEquals(userId);
    }

    @Override
    public void deleteOrderByOrderId(String orderId) {
        //删除订单
        shoppingOrderRepository.deleteById(orderId);
        //删除订单商品表
        List<ShoppingOrderSKU> soss = shoppingOrderSKURepository.findAllByOrderIdEquals(orderId);
        shoppingOrderSKURepository.deleteAll(soss);
    }

}
