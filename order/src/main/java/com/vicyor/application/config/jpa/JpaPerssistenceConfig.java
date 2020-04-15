package com.vicyor.application.config.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;

/**
 * 作者:姚克威
 * 时间:2020/4/5 18:22
 **/
@Configuration
public class JpaPerssistenceConfig {
    @Bean("jpaTransactionManager")
    public JpaTransactionManager jpaTransactionManager() {
        return new JpaTransactionManager();
    }
}
