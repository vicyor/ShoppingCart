package com.vicyor.application.controller;

import com.vicyor.application.common.GeneralResponseObject;
import com.vicyor.application.dto.SKUOrderDTO;
import com.vicyor.application.exception.InsufficientInventoryException;
import com.vicyor.application.exception.UpdateStockFailedException;
import com.vicyor.application.exception.VersionException;
import com.vicyor.application.po.ShoppingSKU;
import com.vicyor.application.service.LocalOrderService;
import com.vicyor.application.service.ShoppingSKUService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 作者:姚克威
 * 时间:2020/3/24 22:21
 **/
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private LocalOrderService localOrderService;
    /**
     * 1.商品模块
     * 1.查库存
     * 2.比对商品版本(因为商品后端价格可能突然改变，但是前端没刷新)
     * 2.订单模块 下订单
     * 2.商品模块 减库存
     * 2.购物车模块  清除购物车
     * 通过本地开启本地事务下订单基于mq(事务), 再向 mq 上发送2个消息(确认机制)
     */
    /**
     * 创建订单
     * 1.验证
     * 1.查库存.
     * 2.比对商品版本.
     * 2.创建订单操作
     * 1.减库存(!!!).
     * 2.生成订单. 向MQ发送信息 生成订单后不用再考虑版本,最终价格由最新版本决定
     * 3.清空购物车. 向MQ发送信息
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
                response = new GeneralResponseObject(500, null, ex.getMessage());
            else if (ex instanceof VersionException) {
                response = new GeneralResponseObject(500, null, ex.getMessage());
            } else {
                response = new GeneralResponseObject(500, null, ex.getMessage());
            }
            return response;
        }
        try {
            //mq开启事务
            rabbitTemplate.setChannelTransacted(true);
            //mq设置消息转化器
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            //执行减库存,向mq队列发送清空服务车,发送持久化订单信息
            localOrderService.executeCreateOrderTask(dtos);
        } catch (Exception e) {
            return response = new GeneralResponseObject(500, null, e.getMessage());
        }
        return response = new GeneralResponseObject(200, null, "订单创建成功");
    }

}
