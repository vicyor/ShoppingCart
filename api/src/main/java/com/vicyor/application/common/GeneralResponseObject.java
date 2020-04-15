package com.vicyor.application.common;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 作者:姚克威
 * 时间:2020/3/16 9:25
 **/
@Data
public class GeneralResponseObject {
    //状态码
    private Integer status;
    private List data;
    private String message;
    public GeneralResponseObject(){

    }
    public GeneralResponseObject(Integer status, List  data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }
}
