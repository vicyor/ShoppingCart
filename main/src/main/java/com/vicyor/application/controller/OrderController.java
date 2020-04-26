package com.vicyor.application.controller;

import com.vicyor.application.common.GeneralResponseObject;
import com.vicyor.application.dto.SKUOrderDTO;
import com.vicyor.application.exception.InsufficientInventoryException;
import com.vicyor.application.exception.VersionException;
import com.vicyor.application.po.ShoppingOrder;
import com.vicyor.application.po.ShoppingOrderSKU;
import com.vicyor.application.po.ShoppingSKU;
import com.vicyor.application.po.ShoppingUser;
import com.vicyor.application.service.LocalOrderService;
import com.vicyor.application.service.OrderService;
import com.vicyor.application.util.ShoppingUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单操作接口
 **/
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private LocalOrderService localOrderService;
    @Autowired
    private ShoppingUtil shoppingUtil;
    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     * 检验商品库存,检验商品版本 -商品模块
     * 减库存  - 商品模块
     * 创建订单 -  订单模块
     * 清空用户购物车 - 购物车模块
     * 一致性问题:  例如: 清空购物车这个动作失败，不能使得创建订单与减库存回滚.因为是在不同的服务器上，事务无法保证.
     * 解决方式:
     * 1.dubbo容错机制.
     * 2.mq 确认机制和重传机制
     *
     * @param dtos 前端传过来的商品的部分信息
     * @return
     */
    @RequestMapping("/create")
    @ResponseBody
    public GeneralResponseObject createOrder(@RequestBody List<SKUOrderDTO> dtos) {
        GeneralResponseObject response = null;
        try {
            //验证SKU的库存
            localOrderService.verifySKUOrderDTOLists(dtos);
        } catch (Exception ex) {
            if (ex instanceof InsufficientInventoryException)
                response = new GeneralResponseObject(501, null, "商品库存不足");
            else {
                response = new GeneralResponseObject(500, null, "创建订单出现异常");
            }
            ex.printStackTrace();
            return response;
        }
        try {
            //mq开启事务
            rabbitTemplate.setChannelTransacted(true);
            //执行减库存,向mq队列发送修改服务车信息,发送持久化订单信息
            localOrderService.executeCreateOrderTask(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            return response = new GeneralResponseObject(500, null, "创建订单出现异常");
        }
        return response = new GeneralResponseObject(0, "订单创建成功", "订单创建成功");
    }

    /**
     * 获取所有的订单
     *
     * @return
     */
    @GetMapping("/listAll")
    @ResponseBody
    public GeneralResponseObject listAllOrders() {
        /**
         * 获取用户ID
         */
        ShoppingUser shoppingUser = shoppingUtil.getShoppingUser();
        Long userId = shoppingUser.getId();
        /**
         * 获取属于该用户的订单
         */
        List<ShoppingOrder> orders = null;
        try {
            orders = orderService.listOrdersByUserId(userId);
            orders.forEach(order -> {
                order.setUsername(shoppingUser.getUsername());
                BigDecimal money = new BigDecimal(0);
                for (ShoppingOrderSKU sos : order.getSkus()) {
                    BigDecimal price = sos.getShoppingSKU().getPrice();
                    money = money.add(price.multiply(new BigDecimal(sos.getCount())));
                }
                order.setMoney(money);
            });
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponseObject(500, null, "出现异常");
        }

        return new GeneralResponseObject(0, orders, "成功获取订单及商品");
    }

    @RequestMapping("/deleteOrder")
    @ResponseBody
    public GeneralResponseObject deleteOrder(@RequestBody Map<String, Object> payLoad) {
        String orderId = (String) payLoad.get("orderId");
        try {
            orderService.deleteOrderByOrderId(orderId);
            return new GeneralResponseObject(0,null,"删除订单成功");
        } catch (Exception e) {
            return new GeneralResponseObject(500, null, "删除订单失败");
        }
    }
}
