package com.vicyor.application.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vicyor.application.dto.SKUOrderDTO;
import com.vicyor.application.dto.UserShoppingCartDTO;
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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Long skuId = userShoppingCart.getShoppingSKU().getSkuId();
        Optional<UserShoppingCart> optional = userShoppingCartRepository.findOneByUserIdEqualsAndShoppingSKU_SkuIdEquals(userId, skuId);
        if (optional.isPresent()) {
            //存在则更新
            userShoppingCartRepository.addUserShoppingCart(userId, skuId, userShoppingCart.getCount());
        } else {
            //不存在则存储
            userShoppingCartRepository.save(userShoppingCart);
        }
    }

    @Override
    public List<UserShoppingCart> getShoppingCartsByUserId(Long userId) {
        UserShoppingCart userShoppingCart = new UserShoppingCart(userId);
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
                    if (userShoppingCart.getShoppingSKU().getSkuId().equals(temporary.getSkuId())) {
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
                userShoppingCartRepository.restoreUserShoppingCart(userId, dto.getSkuId(), dto.getCount());
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
                Long skuId = orderSKU.getShoppingSKU().getSkuId();
                Long restoreCount = orderSKU.getCount();
                userShoppingCartRepository.restoreUserShoppingCart(userId, skuId, restoreCount);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void updateUserShoppingCarts(Long userId, List uCarts) {
        ObjectMapper mapper = new ObjectMapper();
        List<UserShoppingCartDTO> dtos = (List<UserShoppingCartDTO>) uCarts.stream().map(uCart -> {
            UserShoppingCartDTO dto = null;
            try {
                String jsonStr = mapper.writeValueAsString(uCart);
                dto = mapper.readValue(jsonStr, UserShoppingCartDTO.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return dto;
        }).collect(Collectors.toList());
        //更新用户购物车
        for (UserShoppingCartDTO dto : dtos) {
            userShoppingCartRepository.updateUserShoppingCart(dto.getId(), dto.getCount(), dto.getSelected());
        }
        //删除已经删除的数据
        List<UserShoppingCart> carts = userShoppingCartRepository.findAllByUserIdEquals(userId);
        List<Long> cartIds = dtos.stream().map(dto -> dto.getId()).collect(Collectors.toList());
        //过滤出来删除的carts
        List<UserShoppingCart> deletedCarts = carts.stream().filter(cart -> {
            return !cartIds.contains(cart.getId());
        }).collect(Collectors.toList());
        //将前端删除的carts进行删除
        userShoppingCartRepository.deleteAll(deletedCarts);
    }

}
