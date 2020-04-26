package com.vicyor.application.config.rabbit;

import com.rabbitmq.client.Channel;
import com.vicyor.application.service.UserShoppingCartService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 清空购物车
 **/

@Component
public class RabbitMQShoppingcartListener {
    @Reference
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
        Long userId = Long.valueOf(shoppingCartMap.get("userId").toString());
        List dtoList = (ArrayList) shoppingCartMap.get("data");
        Long deliveryTag = Long.valueOf(headers.get(AmqpHeaders.DELIVERY_TAG).toString());
        try {
            //用户购物车减count
            userShoppingCartService.deCreaseTheCountOfUserShoppingCart(userId, dtoList);
        } catch (Exception ex) {
            //数据库操作失败，消息取消
            channel.basicNack(deliveryTag, false, true);
        }

        channel.basicAck(deliveryTag, false);
    }
}
