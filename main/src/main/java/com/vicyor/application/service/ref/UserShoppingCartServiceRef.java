package com.vicyor.application.service.ref;

import com.vicyor.application.po.TemporaryShoppingCart;
import com.vicyor.application.po.UserShoppingCart;
import com.vicyor.application.service.UserShoppingCartService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 作者:姚克威
 * 时间:2020/3/16 15:21
 **/
@Service
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
    public void mergeTemporaryShoppingCart(Long principal, List<TemporaryShoppingCart> temporaryShoppingCarts) {
        userShoppingCartService.mergeTemporaryShoppingCart(principal, temporaryShoppingCarts);
    }
}
