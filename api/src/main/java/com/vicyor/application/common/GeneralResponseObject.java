package com.vicyor.application.common;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 作者:姚克威
 * 时间:2020/3/16 9:25
 **/
@Data
public class GeneralResponseObject {
    /**
     * status
     * 0 -> 成功
     * 1 -> 登录错误
     * 100 -> 密码错误
     * 101 -> 用户不存在
     * 102 -> 其它错误
     * 103 -> 合并临时购物车失败
     * 2 ->  注册错误
     * 200 -> 用户已存在
     * 3 ->  购物车错误
     * 300 -> 商品库存不足
     * 301 -> 购物车其它错误
     * 302 -> 更新用户购物车失败
     * 4 -> sku错误
     * 400 -> 获取sku列表错误
     * 401 -> 获取sku商品错误
     * 5 -> 订单错误
     * 500 -> 订单操作中后端其它错误
     * 501 -> 下订单时候商品库存不足
     */
    private Integer status;
    private Object data;
    private String message;
    public GeneralResponseObject(){

    }
    public GeneralResponseObject(Integer status,Object  data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }
}
