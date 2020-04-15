package com.vicyor.application.exception;

/**
 * 库存异常
 **/
public class InsufficientInventoryException  extends Exception{
    private String  goodsName;
    public InsufficientInventoryException(String goodsName){
        super(goodsName+"库存不足,创建订单失败");
        this.goodsName=goodsName;
    }
}
