package com.vicyor.application.repository;

import com.vicyor.application.po.UserShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserShoppingCartRepository extends JpaRepository<UserShoppingCart,Long> {
    List<UserShoppingCart> findAllByUserIdEquals(Long userId);
}
