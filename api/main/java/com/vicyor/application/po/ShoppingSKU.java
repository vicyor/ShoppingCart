package com.vicyor.application.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 商品表
 **/
@Table(schema = "shopping", name = "shopping_sku")
@Data
@Entity
public class ShoppingSKU {
    //商品ID
    private Long skuId;
    //商品名称
    private String goodsName;
    //商品价格
    private BigDecimal price;
    //库存
    private Long stock;
    //商品版本
    private Long version;
    protected ShoppingSKU(){

    }
}
