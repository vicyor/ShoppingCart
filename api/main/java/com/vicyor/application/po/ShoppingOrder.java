package com.vicyor.application.po;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

/**
 * 作者:姚克威
 * 时间:2020/4/2 18:45
 **/
@Entity
@Data
@Table(schema = "shopping", name = "shopping_order")
public class ShoppingOrder {
    @Id
    private String id;
    private Long userId;
    //创建时间
    private Timestamp createTime;
    //订单状态
    private Integer status;
    @OneToMany
    @JoinColumn(name = "id", referencedColumnName = "order_id", table = "shopping_order_sku")
    private List<ShoppingOrderSKU> skus;

    public ShoppingOrder(String id, Long userId, Timestamp createTime, Integer status) {
        this.id = id;
        this.userId = userId;
        this.createTime = createTime;
        this.status = status;
    }
    protected ShoppingOrder(){

    }
}
