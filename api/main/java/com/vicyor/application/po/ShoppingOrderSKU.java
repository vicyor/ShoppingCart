package com.vicyor.application.po;

import lombok.Data;

import javax.persistence.*;

/**
 * 作者:姚克威
 * 时间:2020/4/2 19:14
 **/
@Entity
@Data
@Table(schema = "shopping",name = "shopping_order_sku")
public class ShoppingOrderSKU {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderId;
    private Long skuId;
    private Long count;
    @OneToOne
    @JoinColumn(name ="skuId",referencedColumnName = "skuId",table = "shopping_sku")
    private ShoppingSKU sku;

    public ShoppingOrderSKU(String orderId, Long skuId, Long count) {
        this.orderId = orderId;
        this.skuId = skuId;
        this.count = count;
    }
    protected ShoppingOrderSKU(){

    }
}
