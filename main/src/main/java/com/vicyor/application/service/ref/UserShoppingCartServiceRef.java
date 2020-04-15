package com.vicyor.application.service.ref;

import com.vicyor.application.po.UserShoppingCart;
import com.vicyor.application.service.UserShoppingCartService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 作者:姚克威
 * 时间:2020/4/5 13:37
 **/
@Service("userShoppingCartServiceRef")
public class UserShoppingCartServiceRef implements UserShoppingCartService {
    @Reference
    UserShoppingCartService userShoppingCartService;

    @Override
    public void saveUserShoppingCart(UserShoppingCart userShoppingCart) {
        userShoppingCartService.saveUserShoppingCart(userShoppingCart);
    }

    @Override
    public List<UserShoppingCart> getShoppingCartsByUserId(Long userId) {
        return userShoppingCartService.getShoppingCartsByUserId(userId);
    }


    @Override
    public void mergeTemporaryShoppingCart(Long userId, List temporaryShoppingCarts) {
        userShoppingCartService.mergeTemporaryShoppingCart(userId, temporaryShoppingCarts);
    }

    @Override
    public void deCreaseTheCountOfUserShoppingCart(Long userId, List dtos) {
        userShoppingCartService.deCreaseTheCountOfUserShoppingCart(userId, dtos);
    }

    @Override
    public void restoreUserShoppingCart(Long userId, List shoppingOrderSKUS) {
        userShoppingCartService.restoreUserShoppingCart(userId,shoppingOrderSKUS);
    }
}
