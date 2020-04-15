package com.vicyor.application;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 作者:姚克威
 * 时间:2020/3/15 19:07
 **/

@EnableJpaRepositories(transactionManagerRef = "jpaTransactionManager",basePackages = "com.vicyor.application.repository")
@SpringBootApplication
@EnableDubbo(scanBasePackages = "com.vicyor.application")
@EnableTransactionManagement
@EnableRabbit
public class CartApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class, args);
    }
}
