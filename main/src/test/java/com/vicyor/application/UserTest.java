package com.vicyor.application;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 作者:姚克威
 * 时间:2020/4/3 15:20
 **/
public class UserTest {
    @Test
    public void testPassword(){
        String password="vicyor123456";
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String encode = encoder.encode(password);
        System.out.println(encode);
    }
    @Test
    public void testRegex(){

        // .*[A-Za-z] 有一个字母  前瞻
        // .*\\d      有一个数字  前瞻
        // \\w{8,}    有字母或数字
        String regex="^(?=.*[A-Za-z])(?=.*\\d)([A-Za-z\\d]{8,})$";
        regex="x(?=a)(?=a)";
        Pattern pattern = Pattern.compile(regex);
        String str="a4#33a123456";
        str="xa";
        Matcher matcher = pattern.matcher(str);
        System.out.println(matcher.find());
    }
}
