package com.vicyor.application.config.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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
     * 功能:减少购物车商品对应的库存
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

    /**
     * 购物车恢复队列
     */
    @Bean
    public Queue cartStockRestoreQueue() {
        Queue queue = new Queue("cart-stock-restore");
        return queue;
    }

    /**
     * 购物车恢复交换机
     */
    @Bean
    public Exchange cartStockRestoreExchange() {
        Exchange exchange = new DirectExchange("cart-stock-restore");
        return exchange;
    }

    /**
     * 购物车恢复交换机与队列绑定
     */
    @Bean
    public Binding cartStockRestoreBinding(@Qualifier("cartStockRestoreQueue") Queue cartStockRestoreQueue, @Qualifier("cartStockRestoreExchange") Exchange cartStockRestoreExchange) {
        return BindingBuilder
                .bind(cartStockRestoreQueue)
                .to(cartStockRestoreExchange)
                .with("cart.stock.restore") //绑定key
                .noargs();
    }


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
