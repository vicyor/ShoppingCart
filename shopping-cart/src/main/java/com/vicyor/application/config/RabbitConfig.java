package com.vicyor.application.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 作者:姚克威
 * 时间:2020/4/3 0:21
 **/
@Configuration
public class RabbitConfig {
    /**
     * 购物车队列
     * 功能:清空购物车
     */
    @Bean
    public Queue shoppingCartQueue() {
        Queue queue = new Queue("shopping-cart");
        return queue;
    }

    /**
     * 订单交换机
     */
    @Bean
    public Exchange shoppingCartExchange() {
        Exchange exchange = new DirectExchange("shopping-cart");
        return exchange;
    }

    /**
     * 订单绑定
     */
    @Bean
    public Binding shoppingCartBinding(@Qualifier("shoppingCartQueue") Queue shoppingCartQueue, @Qualifier("shoppingCartExchange") Exchange shoppingCartExchange) {
        return BindingBuilder
                .bind(shoppingCartQueue)
                .to(shoppingCartExchange)
                .with("shopping.shoppingcart.empty") //绑定key
                .noargs();
    }
}
