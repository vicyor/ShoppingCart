package com.vicyor.application.po;

import lombok.Data;
import org.hibernate.annotations.Proxy;
import org.springframework.context.annotation.Lazy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品表
 **/
@Table(schema = "shopping", name = "shopping_sku")
@Data
@Entity
@Proxy(lazy = false)
public class ShoppingSKU implements Serializable {
    //商品ID
    @Id
    @Column(name = "sku_id")
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
