package com.vicyor.application.po;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 作者:姚克威
 * 时间:2020/4/2 18:45
 **/
@Entity
@Data
@Table(schema = "shopping", name = "shopping_order")
public class ShoppingOrder implements Serializable {
    @Id
    @Column(name = "id")
    private String id;
    private Long userId;
    //创建时间
    private Timestamp createTime;
    //订单状态
    private Integer status;
    @OneToMany
    //1对多 外键在多表
    @JoinColumn(name = "order_id", referencedColumnName = "id")
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
