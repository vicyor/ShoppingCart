package com.vicyor.application.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * 用户购物车
 **/
@Entity
@Data
@Table(schema = "shopping", name = "user_shopping_cart")
public class UserShoppingCart {
    @Id
    //主键自增
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //商品id
    private Long skuId;
    //用户id
    private Long userId;
    //加入购物车的数量，多次加入会进行合并
    private Long count;
    //采购时间
    private Timestamp purchase;
    //勾选状态
    private Integer selected;
    @Transient
    private Long stock;
    public UserShoppingCart(Long skuId, Long userId, Long count, Timestamp purchase, Integer selected) {
        this.skuId = skuId;
        this.userId = userId;
        this.count = count;
        this.purchase = purchase;
        this.selected = selected;
    }
}