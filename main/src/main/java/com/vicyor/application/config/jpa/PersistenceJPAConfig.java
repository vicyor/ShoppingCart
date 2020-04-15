package com.vicyor.application.config.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 作者:姚克威
 * 时间:2020/4/5 10:11
 **/
@Configuration
public class PersistenceJPAConfig {
    @Bean("jpaTransactionManager")
    public JpaTransactionManager jpaTransactionManager() {
        return new JpaTransactionManager();
    }
}
