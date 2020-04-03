package com.vicyor.application.config.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

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

    private static final String TTL_QUEUE = "order-ttl";
    private static final String TTL_EXCHANE = "order-ttl";
    private static final String TTL_BINDING_KEY = "shopping.order.ttl";
    private static final String DEAD_EXCHANGE = "order_dead";
    private static final String DEAD_QUEUE = "order_dead";

    @Bean
    public Exchange ttlExchange() {
        return new DirectExchange(TTL_EXCHANE);
    }

    @Bean
    public Queue ttlQueue() {
        Map<String, Object> params = new HashMap<>();
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //设置队列延迟时间
        params.put("expiration","1000");

        return new Queue(TTL_QUEUE, true, false, false, params);
    }

    @Bean
    public Binding ttlBinding() {
        return BindingBuilder.bind(ttlQueue()).to(ttlExchange()).with(TTL_BINDING_KEY).noargs();
    }

    @Bean
    public Exchange deadExchange(){
        return new FanoutExchange(DEAD_EXCHANGE);
    }

    @Bean
    public Queue deadQueue(){
        return new Queue(DEAD_QUEUE);
    }

    @Bean
    public Binding deadBinding(){
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with("fanout").noargs();
    }
}
