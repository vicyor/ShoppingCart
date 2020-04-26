package com.vicyor.application.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
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
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Timestamp createTime;
    //订单状态
    private Integer status;
    @OneToMany(fetch = FetchType.EAGER)
    //1对多 外键在多表
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private List<ShoppingOrderSKU> skus;
    //订单金额
    @Transient
    private BigDecimal money;
    //订单用户名
    @Transient
    private String username;
    public ShoppingOrder(String id, Long userId, Timestamp createTime, Integer status) {
        this.id = id;
        this.userId = userId;
        this.createTime = createTime;
        this.status = status;
    }
    protected ShoppingOrder(){

    }
}
