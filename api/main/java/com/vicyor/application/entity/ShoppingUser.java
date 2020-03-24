package com.vicyor.application.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 用户
 **/
@Entity
@Table(schema = "shopping", name = "shopping_user")
@Data
public class ShoppingUser {
    private Long userId;
    private String username;
    private String password;
    //角色
    @ManyToMany
    @JoinTable(name = "shopping_user_role",
            joinColumns =@JoinColumn(name = "user_id",referencedColumnName = "id",table = "shopping_user"),
            inverseJoinColumns =@JoinColumn(name = "role_id",referencedColumnName = "id",table = "shopping_role")
    )
    private List<ShoppingRole> roles;
}