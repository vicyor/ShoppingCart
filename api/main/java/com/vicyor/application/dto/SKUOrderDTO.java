package com.vicyor.application.dto;

import lombok.Data;

/**
 * 前端 -> 后端的SKU信息
 **/
@Data
public class SKUOrderDTO {
    //商品Id
    private Long skuId;
    //商品版本
    private Long version;
    //购买数量
    private Long count;
}
