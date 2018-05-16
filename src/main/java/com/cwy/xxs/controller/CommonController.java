package com.cwy.xxs.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * @author acy19
 */
public class CommonController {
    @RequestMapping("hello")
    public String hello(HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return "欢迎使用先小生后台";
    }
}
