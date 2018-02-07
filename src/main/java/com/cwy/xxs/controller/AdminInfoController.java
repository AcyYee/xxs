package com.cwy.xxs.controller;

import com.cwy.xxs.service.AdminInfoService;
import com.cwy.xxs.util.HttpSend;
import com.cwy.xxs.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("adminInfo")
public class AdminInfoController {

    @Autowired
    private AdminInfoService adminInfoService;

    @PostMapping("login")
    public ResultData login(String adminName, String adminPassword, HttpServletRequest request){
        String ip = HttpSend.getRemoteIP(request);
        return adminInfoService.login(adminName,adminPassword,ip);
    }

    @RequestMapping("add")
    public String data(String adminName,String adminPassword){
        return adminInfoService.add(adminName,adminPassword);
    }

    @RequestMapping("getQRCode")
    public ResultData getQRCode(String path){
        return adminInfoService.getQRCode(path);
    }

}
