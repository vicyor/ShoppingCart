package com.vicyor.application.controller;

import com.vicyor.application.common.GeneralResponseObject;
import com.vicyor.application.dto.PurchaseDTO;
import com.vicyor.application.dto.UserShoppingCartDTO;
import com.vicyor.application.po.ShoppingUser;
import com.vicyor.application.po.UserShoppingCart;
import com.vicyor.application.service.ShoppingSKUService;
import com.vicyor.application.service.UserShoppingCartService;
import com.vicyor.application.util.ShoppingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private ShoppingUtil shoppingUtil;

    @RequestMapping("/purchase")
    @ResponseBody
    /**
     * 加购操作
     * 1.商品模块获取库存
     * 2.购物车模块加购
     */
    public GeneralResponseObject addPurchase(
            @RequestBody PurchaseDTO purchaseDTO,
            HttpServletRequest request
    ) {
        //获取登录用户
        ShoppingUser user = shoppingUtil.getShoppingUser();
        Long userId = user != null ? user.getId() : -1;
        //构造返回对象
        GeneralResponseObject response = null;
        List cartList = new ArrayList();
        long skuId = purchaseDTO.getSkuId();
        long count = purchaseDTO.getCount();
        //获取商品库存
        Long skuCount = shoppingSKUService.getStockOfSKU(skuId);
        if (skuCount - count < 0) {
            response = new GeneralResponseObject(300, null, "商品库存不足");
        } else {
            //架构时间
            Timestamp purchase = new Timestamp(System.currentTimeMillis());
            UserShoppingCart userShoppingCart = new UserShoppingCart(skuId, userId, count, purchase, 0);
            //存储购物车数据
            try {
                userShoppingCartService.saveUserShoppingCart(userShoppingCart);
            } catch (Exception e) {
                return new GeneralResponseObject(301, null, "加购出现错误");
            }
            cartList.add(userShoppingCart);
        }
        response = new GeneralResponseObject(0, new ArrayList() {{
            add(cartList);
        }}, "加入购物车成功");
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

    ) {
        ShoppingUser shoppingUser = shoppingUtil.getShoppingUser();
        Long userId = shoppingUser.getId();
        //缓存不存在用户数据时,从数据库加载购物车实例
        //构造响应对象,将购物车实例返回
        try {
            List<UserShoppingCart> userShoppingCarts = userShoppingCartService.getShoppingCartsByUserId(userId);
            return new GeneralResponseObject(
                    0,
                    userShoppingCarts,
                    "成功获取用户购物车"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponseObject(301, null, "获取用户购物车失败");
        }
    }

    @PostMapping("updateUserShoppingCarts")
    @ResponseBody
    public GeneralResponseObject updateUserShoppingCarts(@RequestBody List<UserShoppingCartDTO> uCarts) {
        try {
            ShoppingUser shoppingUser = shoppingUtil.getShoppingUser();
            userShoppingCartService.updateUserShoppingCarts(shoppingUser.getId(), uCarts);
        } catch (Exception e) {
            e.printStackTrace();
            return new GeneralResponseObject(302, null, "更新用户购物车失败");
        }
        return new GeneralResponseObject(0, null, "更新用户购物车成功");
    }
}
