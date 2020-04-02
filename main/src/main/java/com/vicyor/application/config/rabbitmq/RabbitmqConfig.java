package com.vicyor.application.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 作者:姚克威
 * 时间:2020/3/25 11:30
 **/
@Configuration
public class RabbitmqConfig {
    /**
     * 订单队列
     * 功能：创建订单
     */
    @Bean
    public Queue orderQueue() {
        Queue queue = new Queue("order");
        return queue;
    }

    /**
     * 订单交换机
     */
    @Bean
    public Exchange orderExchange() {
        Exchange exchange = new DirectExchange("order");
        return exchange;
    }

    /**
     * 订单绑定
     */
    @Bean
    public Binding orderBinding(@Qualifier("orderQueue") Queue orderQueue, @Qualifier("orderExchange") Exchange orderExchange) {
        return BindingBuilder
                .bind(orderQueue)
                .to(orderExchange)
                .with("shopping.order.create") //绑定key
                .noargs();
    }
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
    @Bean("rabbitTransactionManager")
    public RabbitTransactionManager rabbitTransactionManager(CachingConnectionFactory connectionFactory){
        return new RabbitTransactionManager(connectionFactory);
    }
}

