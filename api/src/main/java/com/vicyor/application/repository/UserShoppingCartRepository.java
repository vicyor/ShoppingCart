package com.vicyor.application.repository;

import com.vicyor.application.dto.SKUOrderDTO;
import com.vicyor.application.po.UserShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserShoppingCartRepository extends JpaRepository<UserShoppingCart,Long> {
    List<UserShoppingCart> findAllByUserIdEquals(Long userId);

    Optional<UserShoppingCart> findOneByUserIdEqualsAndShoppingSKU_SkuIdEquals(Long userId, Long skuId);

    /**
     * 加购若商品已存在则添加其数量
     */
    @Modifying
    @Query(nativeQuery = true,value = "update user_shopping_cart set  count = count + ?3  where user_id = ?1 and sku_id = ?2")
    void addUserShoppingCart(Long userId, Long skuId, Long count);

    /**
     * 订单取消恢复用户购物车
     * @param userId
     * @param skuId
     * @param count
     */
    @Modifying
    @Query(nativeQuery = true,value = "update user_shopping_cart set count =count + ?3 where user_id =?1 and sku_id =?2")
    void restoreUserShoppingCart(Long userId, Long skuId, Long count);

    /**
     * 前端手动更新用户购物车
     */
    @Modifying
    @Query(nativeQuery = true,value = "update user_shopping_cart set count =?2,selected=?3 where id =?1")
    void updateUserShoppingCart(Long id, Long count, Integer selected);
}
