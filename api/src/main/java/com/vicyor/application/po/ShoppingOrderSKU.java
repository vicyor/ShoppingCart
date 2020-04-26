package com.vicyor.application.po;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 作者:姚克威
 * 时间:2020/4/2 19:14
 **/
@Entity
@Data
@Table(schema = "shopping",name = "shopping_order_sku")
public class ShoppingOrderSKU implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_id")
    private String orderId;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sku_id",referencedColumnName = "sku_id")
    private ShoppingSKU shoppingSKU;
    private Long count;

    public ShoppingOrderSKU(String orderId, ShoppingSKU shoppingSKU, Long count) {
        this.orderId = orderId;
        this.shoppingSKU = shoppingSKU;
        this.count = count;
    }
    protected ShoppingOrderSKU(){

    }

    public ShoppingOrderSKU(String orderId) {
        this.orderId = orderId;
    }
}
