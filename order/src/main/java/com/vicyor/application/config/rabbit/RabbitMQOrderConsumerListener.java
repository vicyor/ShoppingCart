package com.vicyor.application.config.rabbit;

import com.rabbitmq.client.Channel;
import com.vicyor.application.dto.SKUOrderDTO;
import com.vicyor.application.po.ShoppingOrder;
import com.vicyor.application.service.OrderService;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
        //对订单进行处理
        ShoppingOrder shoppingOrder = new ShoppingOrder(orderId, userId,
                new Timestamp(System.currentTimeMillis()), 0);
        Long deliveryTag = Long.valueOf(headers.get(AmqpHeaders.DELIVERY_TAG).toString());
        try {
            orderService.handle(shoppingOrder, dtos);
        } catch (Exception e) {
            //数据库操作失败，消息取消
            channel.basicNack(deliveryTag, false, true);
        }
        channel.basicAck(deliveryTag, false);
    }
}
