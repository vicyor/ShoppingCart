package com.vicyor.application.service;

import com.vicyor.application.entity.UserShoppingCart;

import java.util.List;

public interface UserShoppingCartService {
    void saveUserShoppingCart(UserShoppingCart userShoppingCart);

    List<UserShoppingCart> getShoppingCartsByUserId(Long userId);
}
