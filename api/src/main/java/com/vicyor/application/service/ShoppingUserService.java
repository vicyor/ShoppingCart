package com.vicyor.application.service;

import com.vicyor.application.po.ShoppingUser;

public interface ShoppingUserService{
    Long getUserIdByUsername(String username);

    ShoppingUser findUserByUsername(String username);
}
