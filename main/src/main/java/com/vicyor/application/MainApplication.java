package com.vicyor.application;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * 作者:姚克威
 * 时间:2020/3/16 9:41
 **/
@SpringBootApplication
@EnableDubbo(scanBasePackages = "com.vicyor.service")
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
