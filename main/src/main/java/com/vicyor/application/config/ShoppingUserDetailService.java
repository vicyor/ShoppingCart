package com.vicyor.application.config;

import com.vicyor.application.entity.ShoppingRole;
import com.vicyor.application.entity.ShoppingUser;
import com.vicyor.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * AbstractUserDetailsAuthenticationProvider - UsernamePasswordAuthenticationToken
 **/
@Component
public class ShoppingUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = null;
        if (!StringUtils.isEmpty(username)) {
            //根据用户名从数据库中加载用户
            ShoppingUser shoppingUser = userRepository.findUserByUsername(username);
            //获取用户的角色信息
            List<ShoppingRole> roles = shoppingUser.getRoles();
            //将角色封装成Authority
            List<SimpleGrantedAuthority> authorities = roles.stream().map(
                    role -> new SimpleGrantedAuthority( role.getRoleName())
            ).collect(Collectors.toList());
            user = new User(shoppingUser.getUsername(), shoppingUser.getPassword(), authorities);
        }
        return user;
    }
}
