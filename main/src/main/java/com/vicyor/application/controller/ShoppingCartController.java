package com.vicyor.application.controller;

import com.vicyor.application.common.GeneralResponseObject;
import com.vicyor.application.common.TransformUtil;
import com.vicyor.application.entity.UserShoppingCart;
import com.vicyor.application.service.ref.UserShoppingCartServiceRef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 作者:姚克威
 * 时间:2020/3/16 9:43
 * 购物车的几个功能实现
 **/
@Controller
@RequestMapping("/cart")
public class ShoppingCartController {
    @Autowired
    UserShoppingCartServiceRef userShoppingCartServiceRef;

    @PostMapping("/purchase")
    @ResponseBody
    /**
     * 加购操作
     */
    public GeneralResponseObject addPurchase(
            @RequestParam("skuId") Long skuId,
            @RequestParam("count") Long count,
            @RequestParam("userId") Long userId
    ) {
        //架构时间
        Timestamp purchase = new Timestamp(System.currentTimeMillis());
        //存储加购数据
        UserShoppingCart userShoppingCart = new UserShoppingCart(skuId, userId, count, purchase, 0);
        userShoppingCartServiceRef.saveUserShoppingCart(userShoppingCart);
        //构造响应对象,将购物车实例返回
        GeneralResponseObject result = new GeneralResponseObject(200, new ArrayList() {{
            add(userShoppingCart);
        }}, "加入购物车成功");
        return result;
    }

    /**
     * 获取购物车列表操作
     * 缓存中的存储格式
     * shoppingCarts
     */
    @GetMapping("/list")
    @Cacheable(cacheNames = "shoppingCarts", key = "#userId")
    public GeneralResponseObject getShoppingCartList(
            @RequestParam("userId") Long userId
    ) {
        //缓存不存在用户数据时,从数据库加载购物车实例
        List<UserShoppingCart> userShoppingCarts = userShoppingCartServiceRef.getShoppingCartsByUserId(userId);
        //构造响应对象,将购物车实例返回
        GeneralResponseObject response = new GeneralResponseObject(
                200,
                userShoppingCarts,
                "成功获取消息"
        );
        return response;
    }
}
