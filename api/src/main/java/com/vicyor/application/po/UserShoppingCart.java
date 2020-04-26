package com.vicyor.application.po;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户购物车
 **/
@Entity
@Data
@Table(schema = "shopping", name = "user_shopping_cart")
public class UserShoppingCart implements Serializable {
    @Id
    //主键自增
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "sku_id", referencedColumnName = "sku_id")
    private ShoppingSKU shoppingSKU;
    //用户id
    private Long userId;
    //加入购物车的数量，多次加入会进行合并
    private Long count;
    //采购时间
    private Timestamp purchase;
    //勾选状态
    private Integer selected = 0;
    @Transient
    private Long stock;

    public UserShoppingCart(Long skuId, Long userId, Long count, Timestamp purchase, Integer selected) {
        this.shoppingSKU =new ShoppingSKU(skuId);
        this.userId = userId;
        this.count = count;
        this.purchase = purchase;
        this.selected = selected;
    }
    public UserShoppingCart(Long userId){
        this.userId=userId;
    }

    protected UserShoppingCart() {

    }
}