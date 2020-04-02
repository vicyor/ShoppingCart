package com.vicyor.application.exception;

/**
 * 商品版本异常
 **/
public class VersionException  extends Exception{
    private String goodsName;
    public VersionException(String goodsName){
        super(goodsName+"版本出现错误,创建订单失败");
        this.goodsName=goodsName;
    }
}
