package com.vicyor.application.service.impl;

import com.vicyor.application.po.TemporaryShoppingCart;
import com.vicyor.application.po.UserShoppingCart;
import com.vicyor.application.repository.UserShoppingCartRepository;
import com.vicyor.application.service.UserShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
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
    public void saveUserShoppingCart(UserShoppingCart userShoppingCart) {
        //将用户购物车进行存储
        userShoppingCartRepository.save(userShoppingCart);
    }

    @Override
    public List<UserShoppingCart> getShoppingCartsByUserId(Long userId) {
        UserShoppingCart userShoppingCart = new UserShoppingCart(null, userId, null, null, null);
        List<UserShoppingCart> userShoppingCarts = userShoppingCartRepository.findAll(Example.of(userShoppingCart));
        return userShoppingCarts;
    }

    @Override
    public void mergeTemporaryShoppingCart(Long userId, List<TemporaryShoppingCart> temporaryShoppingCarts) {
        //根据userId获取用户购物车实例
        List<UserShoppingCart> userShoppingCarts = userShoppingCartRepository.findAllByUserIdEquals(userId);
        //将用户购物车与临时购物车的sku合并
        Iterator<TemporaryShoppingCart> iterator = temporaryShoppingCarts.iterator();
        while (iterator.hasNext()) {
            TemporaryShoppingCart temporary = iterator.next();
            for (UserShoppingCart user : userShoppingCarts) {
                if (user.getSkuId().equals(temporary.getSkuId())) {
                    //如果用户购物车存在就将其合并
                    user.setCount(user.getCount() + temporary.getCount());
                    iterator.remove();
                } else {
                    //如果用户购物车不存在则将其添加到用户购物车列表
                    userShoppingCarts.add(new UserShoppingCart(temporary.getSkuId(), userId,
                            temporary.getCount(), temporary.getPurchase(), temporary.getSelected()));
                }
            }
        }
        //将合并后的购物车数据存储到数据库
        userShoppingCartRepository.saveAll(userShoppingCarts);
    }

    @Override
    @Transactional(transactionManager = "jpaTransactionManager")
    public void flushUserShoppingCart(Long userId, List<Long> skuIdList) {
        for (Long skuId : skuIdList) {
            UserShoppingCart userShoppingCart = new UserShoppingCart(skuId, userId);
            userShoppingCartRepository.delete(userShoppingCart);
        }
    }
}
