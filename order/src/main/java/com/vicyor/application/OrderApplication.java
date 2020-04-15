package com.vicyor.application;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 作者:姚克威
 * 时间:2020/4/2 20:35
 **/
@SpringBootApplication
@EnableDubbo
@EnableJpaRepositories(transactionManagerRef = "jpaTransactionManager",basePackages = "com.vicyor.application.repository")
@EnableRabbit
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
