package com.vicyor.application.repository;

import com.vicyor.application.po.ShoppingSKU;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ShoppingSKURepository extends JpaRepository<ShoppingSKU, Long> {
    @Modifying
    @Transactional
    @Query(value = "update shopping_sku set stock = stock - :count where sku_id = :skuId and stock - :count >=0",nativeQuery = true)
    int updateSKUStockRightly(@Param("skuId") Long skuId,@Param("count") Long count);
}
