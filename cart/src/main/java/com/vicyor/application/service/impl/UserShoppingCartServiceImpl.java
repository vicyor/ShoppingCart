package com.vicyor.application.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vicyor.application.dto.SKUOrderDTO;
import com.vicyor.application.po.ShoppingOrderSKU;
import com.vicyor.application.po.TemporaryShoppingCart;
import com.vicyor.application.po.UserShoppingCart;
import com.vicyor.application.repository.UserShoppingCartRepository;
import com.vicyor.application.service.UserShoppingCartService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 作者:姚克威
 * 时间:2020/3/16 15:04
 **/
@Service
public class UserShoppingCartServiceImpl implements UserShoppingCartService {
    @Autowired
    private UserShoppingCartRepository userShoppingCartRepository;

    @Override
    @Transactional
    public void saveUserShoppingCart(UserShoppingCart userShoppingCart) {
        Long userId = userShoppingCart.getUserId();
        Long skuId = userShoppingCart.getSkuId();
        Optional<UserShoppingCart> optional = userShoppingCartRepository.findOneByUserIdEqualsAndSkuIdEquals(userId, skuId);
        if (optional.isPresent()) {
            //存在则更新
            userShoppingCartRepository.updateUserShoppingCart(userId, skuId, -1 * userShoppingCart.getCount());
        } else {
            //不存在则存储
            userShoppingCartRepository.save(userShoppingCart);
        }
    }

    @Override
    public List<UserShoppingCart> getShoppingCartsByUserId(Long userId) {
        UserShoppingCart userShoppingCart = new UserShoppingCart(null, userId, null, null, null);
        List<UserShoppingCart> userShoppingCarts = userShoppingCartRepository.findAll(Example.of(userShoppingCart));
        return userShoppingCarts;
    }

    @Override
    @Transactional
    public void mergeTemporaryShoppingCart(Long userId, List temporaryShoppingCarts) {
        //根据userId获取用户购物车实例
        List<UserShoppingCart> userShoppingCarts = userShoppingCartRepository.findAllByUserIdEquals(userId);
        //将用户购物车与临时购物车的sku合并
        Iterator iterator = temporaryShoppingCarts.iterator();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            while (iterator.hasNext()) {
                //将map转为obj
                String json = objectMapper.writeValueAsString(iterator.next());
                TemporaryShoppingCart temporary = objectMapper.readValue(json, TemporaryShoppingCart.class);
                boolean exist = false;
                for (UserShoppingCart userShoppingCart : userShoppingCarts) {
                    //若用户购物车存在改商品，则将临时购物车的数量加到用户客户车数量
                    if (userShoppingCart.getSkuId().equals(temporary.getSkuId())) {
                        userShoppingCart.setCount(userShoppingCart.getCount() + temporary.getCount());
                        userShoppingCart.setSelected(temporary.getSelected());
                        exist = true;
                    }
                }
                //用户购物车不存在该商品,则将临时购物车商品加入用户购物车
                if (!exist) {
                    userShoppingCarts.add(new UserShoppingCart(temporary.getSkuId(), userId, temporary.getCount(), temporary.getPurchase(), temporary.getSelected()));
                }

            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将合并后的购物车数据存储到数据库
        userShoppingCartRepository.saveAll(userShoppingCarts);
    }

    @Override
    @Transactional(transactionManager = "jpaTransactionManager")
    public void deCreaseTheCountOfUserShoppingCart(Long userId, List dtos) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (Object obj : dtos) {
                String json = mapper.writeValueAsString(obj);
                SKUOrderDTO dto = mapper.readValue(json, SKUOrderDTO.class);
                userShoppingCartRepository.updateUserShoppingCart(userId, dto.getSkuId(), dto.getCount());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    @Transactional
    public void restoreUserShoppingCart(Long userId, List shoppingOrderSKUS) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (Object obj : shoppingOrderSKUS) {
                String json = mapper.writeValueAsString(obj);
                ShoppingOrderSKU orderSKU = mapper.readValue(json, ShoppingOrderSKU.class);
                Long skuId = orderSKU.getSkuId();
                Long restoreCount = orderSKU.getCount();
                userShoppingCartRepository.updateUserShoppingCart(userId, skuId, -1 * restoreCount);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
