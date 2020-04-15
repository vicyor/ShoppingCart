package com.vicyor.application.controller;

import com.vicyor.application.common.GeneralResponseObject;
import com.vicyor.application.dto.PurchaseDTO;
import com.vicyor.application.po.ShoppingUser;
import com.vicyor.application.po.TemporaryShoppingCart;
import com.vicyor.application.po.UserShoppingCart;
import com.vicyor.application.service.ShoppingSKUService;
import com.vicyor.application.service.UserShoppingCartService;
import com.vicyor.application.util.ShoppingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者:姚克威
 * 时间:2020/3/16 9:43
 * 购物车的几个功能实现
 **/
@Controller
@RequestMapping("/cart")
public class ShoppingCartController {
    @Autowired
    @Qualifier("userShoppingCartServiceRef")
    private UserShoppingCartService userShoppingCartService;
    @Autowired
    @Qualifier("shoppingSKUServiceRef")
    private ShoppingSKUService shoppingSKUService;

    @PostMapping("/purchase")
    @ResponseBody
    /**
     * 加购操作
     * 1.商品模块获取库存
     * 2.购物车模块加购
     */
    public GeneralResponseObject addPurchase(
            @RequestBody List<PurchaseDTO> purchaseDTOList
    ) {
        //获取登录用户
        ShoppingUser user = ShoppingUtil.getShoppingUser();
        Long userId = user != null ? user.getId() : -1;
        //构造返回对象
        GeneralResponseObject response = null;
        List cartList = new ArrayList();
        boolean success = true;
        for (PurchaseDTO dto : purchaseDTOList) {
            Long skuId = dto.getSkuId();
            Long count = dto.getCount();
            //获取商品库存
            Long skuCount = shoppingSKUService.getStockOfSKU(skuId);
            if (skuCount - count < 0) {
                response = new GeneralResponseObject(500, null, "商品数量不足");
                success = false;
                break;
            } else {
                //架构时间
                Timestamp purchase = new Timestamp(System.currentTimeMillis());
                UserShoppingCart userShoppingCart = new UserShoppingCart(skuId, userId, count, purchase, 0);
                TemporaryShoppingCart temporaryShoppingCart = new TemporaryShoppingCart(skuId, count, purchase, 0);
                //存储购物车数据
                if (user != null) {
                    try {
                        userShoppingCartService.saveUserShoppingCart(userShoppingCart);
                    } catch (Exception e) {
                        return new GeneralResponseObject(500,null,"加购出现错误");
                    }
                    cartList.add(userShoppingCart);
                } else {
                    cartList.add(temporaryShoppingCart);
                }
            }
        }
        if (!success) {
            return response;
        }
        if (user != null) {
            response = new GeneralResponseObject(200, new ArrayList() {{
                add(cartList);
            }}, "加入购物车成功");
        } else {
            //未登录
            response = new GeneralResponseObject(200, new ArrayList() {{
                add(cartList);
            }}, "加入购物车成功");
        }
        return response;
    }

    /**
     * 获取购物车列表操作
     * 1.购物车模块获取用户的购物车列表
     */
    @GetMapping("/list")
    @ResponseBody
    @Cacheable(cacheNames = "shoppingCarts", key = "#userId")
    public GeneralResponseObject getShoppingCartList(
            @RequestParam("userId") Long userId
    ) {
        //缓存不存在用户数据时,从数据库加载购物车实例
        List<UserShoppingCart> userShoppingCarts = userShoppingCartService.getShoppingCartsByUserId(userId);
        //构造响应对象,将购物车实例返回
        GeneralResponseObject response = new GeneralResponseObject(
                200,
                userShoppingCarts,
                "成功获取消息"
        );
        return response;
    }
}
