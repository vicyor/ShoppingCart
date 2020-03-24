package com.vicyor.application.controller;

import com.vicyor.application.common.GeneralResponseObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 作者:姚克威
 * 时间:2020/3/16 9:42
 **/
@Controller
public class IndexController {
    @GetMapping("/login")
    public String  login(
    ){
        return "login";
    }
}
