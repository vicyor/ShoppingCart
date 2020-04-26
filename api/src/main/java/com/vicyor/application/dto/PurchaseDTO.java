package com.vicyor.application.dto;

import lombok.Data;

/**
 * 前端加购传递对象
 **/
@Data
public class PurchaseDTO {
    private Long skuId;
    private Long count;
}
