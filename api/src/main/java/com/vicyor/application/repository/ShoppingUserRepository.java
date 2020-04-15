package com.vicyor.application.repository;

import com.vicyor.application.po.ShoppingUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 作者:姚克威
 * 时间:2020/3/23 12:46
 **/
public interface ShoppingUserRepository extends JpaRepository<ShoppingUser, Long> {
    ShoppingUser findUserByUsernameAndAndPassword(String username, String passwrd);

    ShoppingUser findUserByUsername(String username);
}
