package com.vicyor.application.service;

import com.vicyor.application.po.TemporaryShoppingCart;
import com.vicyor.application.po.UserShoppingCart;

import java.util.List;

public interface UserShoppingCartService {
    void saveUserShoppingCart(UserShoppingCart userShoppingCart);

    List<UserShoppingCart> getShoppingCartsByUserId(Long userId);

    void mergeTemporaryShoppingCart(Long userId, List<TemporaryShoppingCart> temporaryShoppingCarts);

    void flushUserShoppingCart(Long userId, List<Long> skuIdList);
}
