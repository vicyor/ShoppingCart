package com.vicyor.application.util;

import com.vicyor.application.po.ShoppingUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 作者:姚克威
 * 时间:2020/3/24 23:15
 **/
public class ShoppingUtil {
    public static ShoppingUser getShoppingUser() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpSession session = request.getSession();
        return (ShoppingUser) session.getAttribute("user");
    }
}
