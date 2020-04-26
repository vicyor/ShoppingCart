package com.vicyor.application.repository;

import com.vicyor.application.po.ShoppingUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * 作者:姚克威
 * 时间:2020/3/23 12:46
 **/
public interface ShoppingUserRepository extends JpaRepository<ShoppingUser, Long> {
    ShoppingUser findUserByUsernameAndAndPassword(String username, String passwrd);

    ShoppingUser findUserByUsername(String username);
    @Query(nativeQuery = true,value = "select count(*) from shopping_user where username = ?1 ")
    Integer existsByUserName(String username);
}
