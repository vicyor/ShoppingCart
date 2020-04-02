package com.vicyor.application.po;

import lombok.Data;

import java.sql.Timestamp;

/**
 * 本地购物车
 **/
@Data
public class TemporaryShoppingCart {
    //商品Id
    private Long skuId;
    //加购商品数量
    private Long count;
    //采购时间
    private Timestamp purchase;
    //勾选状态
    private Integer selected;
    protected TemporaryShoppingCart(){

    }
}
