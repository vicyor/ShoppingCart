package com.vicyor.application.repository;

import com.vicyor.application.entity.UserShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserShoppingCartRepository extends JpaRepository<UserShoppingCart,Long> {

}
