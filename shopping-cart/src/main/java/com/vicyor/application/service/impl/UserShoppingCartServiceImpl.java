package com.vicyor.application.service.impl;

import com.vicyor.application.entity.UserShoppingCart;
import com.vicyor.application.repository.UserShoppingCartRepository;
import com.vicyor.application.service.UserShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 作者:姚克威
 * 时间:2020/3/16 15:04
 **/
@Service
public class UserShoppingCartServiceImpl implements UserShoppingCartService {
    @Autowired
    private UserShoppingCartRepository userShoppingCartRepository;
    @Override
    public void saveUserShoppingCart(UserShoppingCart userShoppingCart){
        //将用户购物车进行存储
        userShoppingCartRepository.save(userShoppingCart);
    }

    @Override
    public List<UserShoppingCart> getShoppingCartsByUserId(Long userId) {
        UserShoppingCart userShoppingCart=new UserShoppingCart(null,userId,null,null,null);
        List<UserShoppingCart> userShoppingCarts = userShoppingCartRepository.findAll(Example.of(userShoppingCart));
        return userShoppingCarts;
    }
}
