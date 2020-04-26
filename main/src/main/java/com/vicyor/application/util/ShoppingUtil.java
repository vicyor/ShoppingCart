package com.vicyor.application.util;

import com.vicyor.application.config.security.jwt.JwtUtil;
import com.vicyor.application.po.ShoppingUser;
import com.vicyor.application.service.ShoppingUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 作者:姚克威
 * 时间:2020/3/24 23:15
 **/
@Component

public class ShoppingUtil {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ShoppingUserService shoppingUserService;
    public ShoppingUser getShoppingUser() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String authorization = request.getHeader("Authorization");
        String username = jwtUtil.getSubject(authorization);
        ShoppingUser user = shoppingUserService.findUserByUsername(username);
        return user;
    }
}
