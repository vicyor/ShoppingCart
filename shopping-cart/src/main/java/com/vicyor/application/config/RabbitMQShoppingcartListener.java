package com.vicyor.application.config;

import com.rabbitmq.client.Channel;
import com.vicyor.application.service.OrderService;
import com.vicyor.application.service.UserShoppingCartService;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 作者:姚克威
 * 时间:2020/4/3 0:27
 **/

@Component
public class RabbitMQShoppingcartListener {
    @Autowired
    UserShoppingCartService userShoppingCartService;

    @RabbitHandler
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "shopping-cart", durable = "true"),
                    exchange = @Exchange(value = "shopping-cart", durable = "true"),
                    key = "shopping.shoppingcart.empty"
            )
    )
    public void onMessage(@Payload Map<String, Object> shoppingCartMap, Channel channel, @Headers Map<String, Object> headers) throws IOException {
        Long userId = (Long) shoppingCartMap.get("userId");
        List<Long> skuIdList = (List<Long>) shoppingCartMap.get("data");
        Long deliveryTag = Long.valueOf(headers.get(AmqpHeaders.DELIVERY_TAG).toString());
        try {
            userShoppingCartService.flushUserShoppingCart(userId, skuIdList);
        } catch (Exception ex) {
            //数据库操作失败，消息取消
            channel.basicNack(deliveryTag, false, true);
        }
        channel.basicAck(deliveryTag, false);
    }
}
