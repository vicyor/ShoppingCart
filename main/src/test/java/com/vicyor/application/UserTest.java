package com.vicyor.application;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 作者:姚克威
 * 时间:2020/4/3 15:20
 **/
public class UserTest {
    @Test
    public void testPassword(){
        String password="123456";
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String encode = encoder.encode(password);
        System.out.println(encode);
    }
}
