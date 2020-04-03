package com.vicyor.application.config.rabbit;

import com.rabbitmq.client.Channel;
import com.vicyor.application.dto.SKUOrderDTO;
import com.vicyor.application.po.ShoppingOrder;
import com.vicyor.application.service.OrderService;
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
import java.util.List;
import java.util.Map;

/**
 * 作者:姚克威
 * 时间:2020/4/2 21:55
 **/
@Component
public class RabbitMQOrderConsumerListener {
    @Autowired
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
        List<SKUOrderDTO> dtos = (List<SKUOrderDTO>) order.get("data");
        Long userId = (Long) order.get("userId");
        String orderId = (String) order.get("id");
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
        //设置发送者重试机制
        rabbitTemplate.setConfirmCallback(((correlationData, ack, casue) -> {
            if(!ack){
                String oId = correlationData.getId();
                rabbitTemplate.convertAndSend("order-ttl","shopping.order.ttl",oId);
            }
        }));
        //消费者对消息签收
        channel.basicAck(deliveryTag, false);
        //data为发送失败时传递的上下文
        CorrelationData data = new CorrelationData();
        data.setId(orderId);
        //将订单Id发往延迟队列
        rabbitTemplate.convertAndSend("order-ttl","shopping.order.ttl",orderId,data);

    }
}
