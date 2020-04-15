package com.vicyor.application.po;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 用户
 **/
@Entity
@Table(schema = "shopping", name = "shopping_user")
@Data
public class ShoppingUser implements Serializable {
    @Id
    private Long id;
    private String username;
    private String password;
    //角色
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "shopping_user_role",
            joinColumns =@JoinColumn(name = "user_id"),
            inverseJoinColumns =@JoinColumn(name = "role_id")
    )
    private List<ShoppingRole> roles;
    protected ShoppingUser(){

    }
}