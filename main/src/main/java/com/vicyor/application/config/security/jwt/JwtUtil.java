package com.vicyor.application.config.security.jwt;

import com.vicyor.application.po.ShoppingRole;
import com.vicyor.application.po.ShoppingUser;
import com.vicyor.application.service.ShoppingUserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 作者:姚克威
 * 时间:2020/4/21 13:56
 **/
@Component
public class JwtUtil {
    private Long expiration = 7200000l;
    private String secret = "vicyor";
    @Autowired
    private ShoppingUserService shoppingUserService;

    private SecretKey generalKey() {
        return new SecretKeySpec(secret.getBytes(), 0, secret.getBytes().length, "AES");
    }

    /**
     * 根据ShoppingUser生成JWT
     *
     * @param shoppingUser
     * @return
     */
    public String generateJsonWebToken(ShoppingUser shoppingUser) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("roles", shoppingUser.getRoles().stream().map(ShoppingRole::getRolename).collect(Collectors.toList()));
        claims.put("username",shoppingUser.getUsername());
        return Jwts
                .builder()
                .setSubject(shoppingUser.getUsername())
                //额外的附加信息
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, generalKey())
                .setIssuedAt(new Date())
                .setExpiration(new java.sql.Date(System.currentTimeMillis() + expiration))
                .compact();
    }

    /**
     * 验证header的token是否合法
     *
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        token = clearToken(token);
        boolean validate = true;
        Claims claims = getClaims(token);
        String username = (String) claims.get("username");
        boolean exist = shoppingUserService.isExistUser(username);
        if (!exist) {
            validate = false;
        } else {
            Date expirationDate = claims.getExpiration();
            boolean expired = expirationDate.before(new Date());
            if (expired) {
                validate = false;
            }
        }
        return validate;
    }

    public String clearToken(String token) {
        return token.substring(7);
    }

    private Claims getClaims(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(generalKey())
                .parseClaimsJws(token).getBody();
        return claims;
    }

    public String getSubject(String token) {
        token = clearToken(token);
        Claims claims = getClaims(token);
        return (String) claims.get("username");
    }

    public List<String> getRoles(String token) {
        token = clearToken(token);
        Claims claims = getClaims(token);
        return (List<String>) claims.get("roles");
    }
}
