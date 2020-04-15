package com.vicyor.application.config.rabbit;

import com.rabbitmq.client.Channel;
import com.vicyor.application.dto.SKUOrderDTO;
import com.vicyor.application.po.ShoppingOrder;
import com.vicyor.application.service.OrderService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建订单
 **/
@Component
public class RabbitMQOrderConsumerListener {
    @Reference
    private OrderService orderService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "order", durable = "true"),
            exchange = @Exchange(value = "order", durable = "true"),
            key = "shopping.order.create"
    ))
    public void onMessage(@Payload Map<String, Object> order, Channel channel, @Headers Map<String, Object> headers) throws IOException {
        //从消息payload中获取订单id，用户id,商品id,购买数量
        List dtos = (ArrayList) order.get("data");
        Long userId = Long.valueOf(order.get("userId").toString());
        String orderId = (String) order.get("orderId");
        ShoppingOrder shoppingOrder = new ShoppingOrder(orderId, userId,
                new Timestamp(System.currentTimeMillis()), 0);
        Long deliveryTag = Long.valueOf(headers.get(AmqpHeaders.DELIVERY_TAG).toString());
        try {
            //将订单，订单与商品多对多表持久化
            orderService.handle(shoppingOrder, dtos);
        } catch (Exception e) {
            //数据库操作失败，消息取消
            channel.basicNack(deliveryTag, false, true);
            throw e;
        }
        //消费者对消息签收
        channel.basicAck(deliveryTag, false);
        String queue = "order-ttl";
        Map<String, Object> data = new HashMap<>();
        data.put("orderId", orderId);
        data.put("userId", userId);
        //将订单信息发往延迟队列
        rabbitTemplate.convertAndSend(queue, "shopping.order.ttl", data);
    }
}
