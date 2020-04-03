package com.vicyor.application.repository;

import com.vicyor.application.po.ShoppingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * 作者:姚克威
 * 时间:2020/4/2 22:34
 **/
public interface ShoppingOrderRepository extends JpaRepository<ShoppingOrder, String> {
    @Modifying
    @Query(value = "update shopping_order set status =2 where id= ?1 and status = 1",nativeQuery = true)
    void cancelOrderIfNecessary(@Param("orderId") String orderId);
}
