package com.cwy.xxs.controller;

import com.cwy.xxs.service.PersonInfoService;
import com.cwy.xxs.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.cwy.xxs.util.HttpSend.getRemoteIP;

/**
 * @author acy19
 */
@RestController
@RequestMapping("personInfo")
public class PersonInfoController {

    @Autowired
    private PersonInfoService personInfoService;

    @PostMapping("wx/login")
    public ResultData login(String code, HttpServletRequest request){
        String ip = getRemoteIP(request);
        return personInfoService.wxLogin(code,ip);
    }

    @PostMapping("wx/find")
    public ResultData find(String id){
        return personInfoService.wxfind(id);
    }

    @PostMapping("wx/registerVIP")
    public ResultData registerVIP(String id, String userPhone, String phoneCode, String realName,String userAddress,HttpServletRequest request){
        String ip = getRemoteIP(request);
        return personInfoService.registerVIP(id,userPhone,phoneCode,realName,userAddress,ip);
    }

    @PostMapping("wx/setUserInfo")
    public ResultData setUserInfo(String id,String wxNico,String wxIcon,String wxGender,String wxProvince,String wxCity,String wxCountry,HttpServletRequest request){
        String ip = getRemoteIP(request);
        return personInfoService.setUserInfo(id,wxNico,wxIcon,wxGender,wxProvince,wxCity,wxCountry,ip);
    }

}
