package com.vicyor.application.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者:姚克威
 * 时间:2020/3/16 17:20
 **/
public class TransformUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    public static Map objToMap(Object obj) {
        HashMap objMap = null;
        try {
            String jsonStr = mapper.writeValueAsString(obj);
            objMap = mapper.readValue(jsonStr, HashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objMap;
    }
}
