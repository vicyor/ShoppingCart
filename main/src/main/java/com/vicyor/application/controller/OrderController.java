package com.vicyor.application.controller;

import com.vicyor.application.common.GeneralResponseObject;
import com.vicyor.application.dto.SKUOrderDTO;
import com.vicyor.application.exception.InsufficientInventoryException;
import com.vicyor.application.exception.VersionException;
import com.vicyor.application.service.LocalOrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
    @PostMapping("/create")
    @ResponseBody
    public GeneralResponseObject createOrder(@RequestBody List<SKUOrderDTO> dtos) {

        GeneralResponseObject response = null;
        try {
            //验证SKU的库存以及版本
            localOrderService.verifySKUOrderDTOLists(dtos);
        } catch (Exception ex) {
            if (ex instanceof InsufficientInventoryException)
                response = new GeneralResponseObject(500, null, ex.toString());
            else if (ex instanceof VersionException) {
                response = new GeneralResponseObject(500, null, ex.toString());
            } else {
                response = new GeneralResponseObject(500, null, ex.toString());
            }
            return response;
        }
        try {
            //mq开启事务
            rabbitTemplate.setChannelTransacted(true);
            //执行减库存,向mq队列发送修改服务车信息,发送持久化订单信息
            localOrderService.executeCreateOrderTask(dtos);
        } catch (Exception e) {
            return response = new GeneralResponseObject(500, null, e.toString());
        }
        return response = new GeneralResponseObject(200, null, "订单创建成功");
    }

}
