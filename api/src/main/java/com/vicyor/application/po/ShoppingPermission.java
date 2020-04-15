package com.vicyor.application.po;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 作者:姚克威
 * 时间:2020/3/23 17:25
 **/
@Entity
@Table(schema = "shopping",name = "shopping_permission")
@Data
public class ShoppingPermission implements Serializable {
    @Id
    private Long id;
    private String url;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "shopping_role_permission",
            joinColumns = @JoinColumn(name = "permission_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<ShoppingRole> roles;
    protected ShoppingPermission(){

    }
}
