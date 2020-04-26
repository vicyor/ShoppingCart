package com.vicyor.application.dto;

import lombok.Data;

/**
 * 订单结算时候传递给购物车模块减库存使用
 **/
@Data
public class SKUOrderDTO {
    //商品Id
    private Long skuId;
    //购买数量
    private Long count;
}
