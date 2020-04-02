package com.vicyor.application.config.security;

import com.vicyor.application.po.ShoppingUser;
import com.vicyor.application.service.ShoppingUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 作者:姚克威
 * 时间:2020/3/24 22:59
 **/
@Component
public class ShoppingAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private ShoppingUserService shoppingUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        //从token中获取用户名
        String username = (String) authentication.getPrincipal();
        //根据用户名从db中加载用户
        ShoppingUser user = shoppingUserService.findUserByUsername(username);
        //将用户放到会话中
        session.setAttribute("user", user);
    }
}
