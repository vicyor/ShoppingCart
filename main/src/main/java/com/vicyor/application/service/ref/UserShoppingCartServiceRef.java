package com.vicyor.application.service.ref;

import com.vicyor.application.entity.UserShoppingCart;
import com.vicyor.application.service.UserShoppingCartService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 作者:姚克威
 * 时间:2020/3/16 15:21
 **/
@Component
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
}
