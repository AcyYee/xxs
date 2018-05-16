package com.cwy.xxs.controller;

import com.cwy.xxs.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("wxPay")
public class PayController {

    private final OrderInfoService orderInfoService;

    @Autowired
    public PayController(OrderInfoService orderInfoService) {
        this.orderInfoService = orderInfoService;
    }

    @RequestMapping(value = "payBack")
    public String payBack(@RequestBody String xmlString){
        return orderInfoService.wxPayBack(xmlString);
    }

    @RequestMapping(value = "refundBack")
    public String refundBack(@RequestBody String xmlString){
        return orderInfoService.wxRefundBack(xmlString);
    }

}
