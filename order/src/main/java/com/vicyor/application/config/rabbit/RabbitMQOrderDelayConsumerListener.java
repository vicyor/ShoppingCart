package com.vicyor.application.config.rabbit;

import com.rabbitmq.client.Channel;
import com.vicyor.application.service.OrderService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 订单过期处理
 */
@Component
public class RabbitMQOrderDelayConsumerListener {
    @Reference
    private OrderService orderService;

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "order_dead", durable = "true"),
            exchange = @Exchange(value = "order_dead"),
            key = "shopping.order.ttl"

    ))
    public void onMessage(@Payload Map<String, Object> dataMap, Channel channel, @Headers Map<String, Object> headers) throws Exception {
        Long deliveryTag = Long.valueOf(headers.get(AmqpHeaders.DELIVERY_TAG).toString());
        try {
            String orderId = (String) dataMap.get("orderId");
            Long userId = Long.valueOf(dataMap.get("userId").toString());
            //若订单未支付则取消订单并恢复库存
            orderService.cancelOrderIfNecessary(orderId, userId);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, true);
            throw e;
        }
        channel.basicAck(deliveryTag, false);
    }
}
