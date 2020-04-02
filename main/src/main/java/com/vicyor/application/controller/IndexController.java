package com.vicyor.application.controller;

import com.vicyor.application.common.GeneralResponseObject;
import com.vicyor.application.po.ShoppingUser;
import com.vicyor.application.po.TemporaryShoppingCart;
import com.vicyor.application.service.ShoppingUserService;
import com.vicyor.application.service.UserShoppingCartService;
import com.vicyor.application.util.ShoppingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 功能处理
 **/
@Controller
public class IndexController {
    @Autowired
    private UserShoppingCartService userShoppingCartService;
    @Autowired
    private ShoppingUserService shoppingUserService;

    /**
     * 前往登录页面
     * @return
     */
    @GetMapping("/login")
    public String login(
    ) {
        //前往登录页面
        return "login";
    }

    /**
     * 登录成功
     * @param temporaryShoppingCarts
     * @return
     */
    @RequestMapping("/success")
    @ResponseBody
    public GeneralResponseObject loginSuccessProcess(@RequestBody List<TemporaryShoppingCart> temporaryShoppingCarts) {
        //获取用户id
        ShoppingUser user = ShoppingUtil.getShoppingUser();
        Long userId =user.getUserId();
        //将临时购物车和用户购物车合并
        userShoppingCartService.mergeTemporaryShoppingCart(userId, temporaryShoppingCarts);
        GeneralResponseObject response = new GeneralResponseObject(200, null, "购物车已经成功合并");
        return response;
    }
}
