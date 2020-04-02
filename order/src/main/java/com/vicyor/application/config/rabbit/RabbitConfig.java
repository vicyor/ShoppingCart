package com.vicyor.application.config.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 作者:姚克威
 * 时间:2020/4/2 20:41
 **/
@Configuration
public class RabbitConfig {
    //订单队列
    @Bean
    public Queue orderQueue() {
        return new Queue("order");
    }

    //订单交换机
    @Bean
    public Exchange orderExchange() {
        return new DirectExchange("order");
    }

    //绑定
    @Bean
    public Binding orderBinding(@Qualifier("orderQueue") Queue orderQueue, @Qualifier("orderExchange") Exchange orderExchange) {
        return BindingBuilder
                .bind(orderQueue)
                .to(orderExchange)
                .with("shopping.order.create") //绑定key
                .noargs();
    }
}
