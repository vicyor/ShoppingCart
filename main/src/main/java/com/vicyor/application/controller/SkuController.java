package com.vicyor.application.controller;

import com.vicyor.application.common.GeneralResponseObject;
import com.vicyor.application.po.ShoppingSKU;
import com.vicyor.application.service.ShoppingSKUService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 作者:姚克威
 * 时间:2020/4/20 10:25
 **/
@Controller
@RequestMapping("/sku")
public class SkuController {
    @Autowired
    @Qualifier("shoppingSKUServiceRef")
    private ShoppingSKUService shoppingSKUService;

    /**
     * 根据skuId获取SKU并返回
     *
     * @param skuId
     * @return
     */
    @RequestMapping("/sku")
    @ResponseBody
    public GeneralResponseObject getSku(@RequestParam Long skuId) {
        ShoppingSKU sku = null;
        try {
            sku = shoppingSKUService.getSKUBySKUId(skuId);

            return new GeneralResponseObject(0, sku, "获取商品成功");
        } catch (Exception e) {
            return new GeneralResponseObject(401, null, "获取sku错误");
        }

    }

    @RequestMapping("/list")
    @ResponseBody
    public GeneralResponseObject getSkuList(@RequestParam("from") Integer from, @RequestParam("size") Integer size) {
        Map<String, Object> response = null;
        try {
            response = shoppingSKUService.getSkus(from, size);
            return new GeneralResponseObject(0, response, "获取sku列表成功");
        } catch (Exception e) {
            return new GeneralResponseObject(400, null, "出现错误");
        }
    }
}
