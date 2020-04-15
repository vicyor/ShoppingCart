package com.vicyor.application.po;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 作者:姚克威
 * 时间:2020/3/23 14:08
 **/
@Entity
@Data
@Table(schema = "shopping",name = "shopping_role")
public class ShoppingRole implements Serializable {
    @Id
    private Long id;
    private String rolename;
    protected ShoppingRole(){

    }
}
