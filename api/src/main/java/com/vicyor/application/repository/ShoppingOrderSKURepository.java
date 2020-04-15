package com.vicyor.application.repository;

import com.vicyor.application.po.ShoppingOrderSKU;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 作者:姚克威
 * 时间:2020/4/2 23:31
 **/
public interface ShoppingOrderSKURepository extends JpaRepository<ShoppingOrderSKU,Long> {
    List<ShoppingOrderSKU> findAllByOrderIdEquals(String orderId);
}
