package com.vicyor.application.repository;

import com.vicyor.application.po.ShoppingOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 作者:姚克威
 * 时间:2020/4/2 22:34
 **/
public interface ShoppingOrderRepository extends JpaRepository<ShoppingOrder,String> {

}
