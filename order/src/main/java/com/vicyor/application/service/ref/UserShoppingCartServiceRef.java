package com.vicyor.application.service.ref;

import com.vicyor.application.po.ShoppingOrderSKU;
import com.vicyor.application.po.UserShoppingCart;
import com.vicyor.application.service.UserShoppingCartService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 作者:姚克威
 * 时间:2020/4/6 13:53
 **/
@Service
public class UserShoppingCartServiceRef implements UserShoppingCartService {
    @Reference
    UserShoppingCartService ref;


    @Override
    public void saveUserShoppingCart(UserShoppingCart userShoppingCart) {
        ref.saveUserShoppingCart(userShoppingCart);
    }

    @Override
    public List<UserShoppingCart> getShoppingCartsByUserId(Long userId) {
        return ref.getShoppingCartsByUserId(userId);
    }

    @Override
    public void mergeTemporaryShoppingCart(Long userId, List temporaryShoppingCarts) {
        ref.mergeTemporaryShoppingCart(userId, temporaryShoppingCarts);
    }

    @Override
    public void deCreaseTheCountOfUserShoppingCart(Long userId, List dtos) {
        ref.deCreaseTheCountOfUserShoppingCart(userId, dtos);
    }

    @Override
    public void restoreUserShoppingCart(Long userId, List shoppingOrderSKUS) {
        ref.restoreUserShoppingCart(userId, shoppingOrderSKUS);
    }
}
