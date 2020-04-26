package com.vicyor.application.config.security;

import com.vicyor.application.config.security.jwt.JwtAuthorizationFilter;
import com.vicyor.application.repository.ShoppingPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Customizer;
import org.springframework.cglib.core.internal.CustomizerRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
    @Autowired
    JwtAuthorizationFilter jwtAuthorizationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                //处理post请求时的options请求
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                //main服务器上的请求都需要认证
                .antMatchers("/isLogin").permitAll()
                .anyRequest().authenticated()
                .and()
                //添加JWT过滤器配置
                .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                //form表单登录配置
                .formLogin()
                //post方式提交认证请求
                .loginProcessingUrl("/login")
                //认证成功处理页面
                .successForwardUrl("/success")
                .permitAll()
                .and()
                //无会话
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //csrf要求表单要有一个hidden域
                .csrf().disable()
                //允许跨域
                .cors();
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

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

}

