package com.vicyor.application.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新用户购物车时前端传递的对象
 * 以及与购物车模块通信传递的对象
 **/
@Data
public class UserShoppingCartDTO implements Serializable {
    //购物车ID
    private Long id;
    private Long count;
    private Integer selected;
}
