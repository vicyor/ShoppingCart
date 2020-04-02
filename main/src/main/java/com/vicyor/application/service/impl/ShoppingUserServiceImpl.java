package com.vicyor.application.service.impl;

import com.vicyor.application.po.ShoppingUser;
import com.vicyor.application.repository.ShoppingUserRepository;
import com.vicyor.application.service.ShoppingUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 作者:姚克威
 * 时间:2020/3/24 15:11
 **/
@Service
public class ShoppingUserServiceImpl implements ShoppingUserService {
    @Autowired
    private ShoppingUserRepository shoppingUserRepository;
    @Override
    public Long getUserIdByUsername(String username) {
        return shoppingUserRepository.findUserByUsername(username).getUserId();
    }

    @Override
    public ShoppingUser findUserByUsername(String username) {
        return shoppingUserRepository.findUserByUsername(username);
    }
}
