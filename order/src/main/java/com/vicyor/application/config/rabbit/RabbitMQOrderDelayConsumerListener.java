package com.vicyor.application.config.rabbit;

import com.rabbitmq.client.Channel;
import com.vicyor.application.service.OrderService;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class RabbitMQOrderDelayConsumerListener {
    @Autowired
    private OrderService orderService;

    @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "order_dead", durable = "true"),
            exchange = @Exchange(value = "order_dead", durable = "true"),
            key = "shopping.order.ttl"

    ))
    public void onMessage(@Payload String orderId, Channel channel, @Headers Map<String, Object> headers) throws IOException {
        Long deliveryTag = Long.valueOf(headers.get(AmqpHeaders.DELIVERY_TAG).toString());
        try {
            orderService.cancelOrderIfNecessary(orderId);
        } catch (Exception e) {
            channel.basicNack(deliveryTag, false, true);
            throw  e;
        }
        channel.basicAck(deliveryTag,false);

    }
}
