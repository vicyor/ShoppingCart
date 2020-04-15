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
    @Modifying
    @Query(nativeQuery = true,value = "update user_shopping_cart set count =count - ?3 where user_id =?1 and sku_id = ?2  ")
    void updateUserShoppingCart(Long userId, Long skuId,Long count);

    Optional<UserShoppingCart> findOneByUserIdEqualsAndSkuIdEquals(Long userId, Long skuId);
}
