package com.vicyor.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vicyor.application.common.GeneralResponseObject;
import com.vicyor.application.config.security.jwt.JwtUtil;
import com.vicyor.application.po.ShoppingUser;
import com.vicyor.application.po.TemporaryShoppingCart;
import com.vicyor.application.service.ShoppingUserService;
import com.vicyor.application.service.UserShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 项目主要功能
 **/
@Controller
public class IndexController {
    @Autowired
    private ShoppingUserService shoppingUserService;
    @Autowired
    @Qualifier("userShoppingCartServiceRef")
    private UserShoppingCartService userShoppingCartService;

    /**
     * Get请求,跳转到登录页面
     *
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(
    ) {
        //前往登录页面
        return "login";
    }

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 登录成功处理
     * 1.将用户存到会话中
     * 2.将临时购物车和用户购物车合并
     */
    @RequestMapping(name = "/success", method = RequestMethod.POST)
    @ResponseBody
    public GeneralResponseObject loginSuccessProcess(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("temporaryShoppingCart") String temporaryShoppingCart) throws IOException {
        //获取用户id
        ShoppingUser user = shoppingUserService.findUserByUsername(username);
        Long userId = user.getId();
        ObjectMapper mapper = new ObjectMapper();
        List temporaryShoppingCarts = mapper.readValue(temporaryShoppingCart, ArrayList.class);
        //将临时购物车和用户购物车合并
        GeneralResponseObject response = new GeneralResponseObject(0, new ArrayList() {{
            add(new HashMap<String, Object>() {{
                put("Authorization", "Bearer " + jwtUtil.generateJsonWebToken(user));
            }});
        }}, "购物车成功合并");
        try {
            userShoppingCartService.mergeTemporaryShoppingCart(userId, temporaryShoppingCarts);
        } catch (RuntimeException re) {
            re.printStackTrace();
            response = new GeneralResponseObject(103, null, "购物车合并失败");
        }
        return response;
    }

    /**
     * 判断用户是否登录
     * 前端判断是否出现302异常，若出现则未登录.
     */
    @RequestMapping("/isLogin")
    @ResponseBody
    public boolean isLogin() {
        return !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }
}
