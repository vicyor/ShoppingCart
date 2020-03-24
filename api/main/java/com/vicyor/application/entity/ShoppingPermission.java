package com.vicyor.application.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 作者:姚克威
 * 时间:2020/3/23 17:25
 **/
@Entity
@Table(schema = "shopping",name = "shopping_permission")
@Data
public class ShoppingPermission {
    private Long id;
    private String url;
    @ManyToMany
    @JoinTable(name = "shopping_role_permission",
            joinColumns = @JoinColumn(name = "permission_id",referencedColumnName ="id",table = "shopping_permission"),
            inverseJoinColumns = @JoinColumn(name = "role_id",referencedColumnName = "id",table = "shopping_role")
    )
    private List<ShoppingRole> roles;
}
