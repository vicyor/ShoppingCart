package com.vicyor.application.service;

import com.vicyor.application.dto.UserShoppingCartDTO;
import com.vicyor.application.po.UserShoppingCart;

import java.util.List;

public interface UserShoppingCartService {
    void saveUserShoppingCart(UserShoppingCart userShoppingCart);

    List<UserShoppingCart> getShoppingCartsByUserId(Long userId);

    void mergeTemporaryShoppingCart(Long userId, List  temporaryShoppingCarts);

    void deCreaseTheCountOfUserShoppingCart(Long userId, List dtos);

    void restoreUserShoppingCart(Long userId, List  shoppingOrderSKUS)  ;

    void updateUserShoppingCarts(Long userId, List  uCarts);
}
