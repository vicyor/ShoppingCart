package com.vicyor.application.config.security;

import com.vicyor.application.po.ShoppingPermission;
import com.vicyor.application.po.ShoppingRole;
import com.vicyor.application.repository.ShoppingPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 动态url权限配置，可以做到应用的url权限随着数据库表数据改变而变化
 * 还可以通过bitmap实现
 * 权限数据源
 **/
@Service
public class ShoppingFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private ShoppingPermissionRepository repository;
    //map缓存URL与其权限关系，URL通过antMatcher匹配
    private volatile Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;

    /**
     * 初始化url权限数据
     * 保证在getAttributes前处理好数据
     */

    public ShoppingFilterInvocationSecurityMetadataSource() {
        Map<RequestMatcher, Collection<ConfigAttribute>> map = new HashMap<>();
        //从db中获取所有的url并缓存起来
        List<ShoppingPermission> permissions = repository.findAll();
        permissions.stream().forEach(permission -> {
            String url = permission.getUrl();
            AntPathRequestMatcher matcher = new AntPathRequestMatcher(url);
            List<ShoppingRole> roles = permission.getRoles();
            List<ConfigAttribute> configs = roles.stream().map(role -> role.getRoleName()).map(SecurityConfig::new).collect(Collectors.toList());
            map.put(matcher, configs);
        });
        this.requestMap = map;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi= (FilterInvocation) object;
        HttpServletRequest request = fi.getRequest();
        Collection<ConfigAttribute>collection=null;
        for (Map.Entry<RequestMatcher,Collection<ConfigAttribute>> entry:requestMap.entrySet()){
            if(entry.getKey().matches(request)){
                collection=entry.getValue();
            }
        }
        return collection;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
