package com.cwy.xxs.controller;

import com.cwy.xxs.entity.AdminInfo;
import com.cwy.xxs.service.AdminInfoService;
import com.cwy.xxs.util.HttpSend;
import com.cwy.xxs.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("adminInfo")
public class AdminInfoController {

    private final AdminInfoService adminInfoService;

    @Autowired
    public AdminInfoController(AdminInfoService adminInfoService) {
        this.adminInfoService = adminInfoService;
    }

    @PostMapping("login")
    public ResultData login(@RequestBody AdminInfo adminInfo, HttpServletRequest request){
        String ip = HttpSend.getRemoteIP(request);
        return adminInfoService.login(adminInfo.getAdminName(),adminInfo.getAdminPassword(),ip);
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
