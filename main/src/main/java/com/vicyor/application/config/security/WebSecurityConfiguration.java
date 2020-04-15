package com.vicyor.application.config.security;

import com.vicyor.application.repository.ShoppingPermissionRepository;
import com.vicyor.application.repository.ShoppingUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * spring securty配置类
 **/
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter implements ApplicationContextAware {
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ShoppingPermissionRepository shoppingPermissionRepository;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                //main服务器上的请求都需要认证
                .anyRequest().authenticated()
                .and()
                //form表单登录配置
                .formLogin()
                //get方式前往表单登录页
                .loginPage("/login")
                //post方式提交认证请求
                .loginProcessingUrl("/login")
                //认证成功处理页面
                .successForwardUrl("/success")
                .permitAll()
                .and()
                //csrf要求表单要有一个hidden域
                .csrf().disable()
                //允许跨域
                .cors().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

}

