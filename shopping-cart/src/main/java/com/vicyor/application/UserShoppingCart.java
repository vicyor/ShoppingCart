package com.vicyor.application;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * 作者:姚克威
 * 时间:2020/3/15 20:50
 **/
@Entity
@Data
@Table(schema = "shopping",name = "user_shopping_cart")
public class UserShoppingCart {
    @Id
    //主键自增
    private Long id;
    private Long SKUId;
    //加入购物车的数量，多次加入会进行合并
    private Long count;
    //采购时间
    private Timestamp purchase;
    //勾选状态
    private Byte selected;
}
