package com.cwy.xxs.controller;

import com.cwy.xxs.service.PhoneCodeService;
import com.cwy.xxs.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Acy
 * @version 1.0
 */

@RestController
@RequestMapping("phoneCode")
public class PhoneCodeController {

    @Autowired
    private PhoneCodeService phoneCodeService;

    @PostMapping("wx/getWXPhone")
    public ResultData getWXPhone(String id,String iv,String encryptedData){
        return phoneCodeService.getWXPhone(id,iv,encryptedData);
    }

    @PostMapping("wx/sendSms")
    public ResultData sendSms(String personId, String phoneNumber, Integer codeType){
        return phoneCodeService.sendSms(personId,phoneNumber,codeType);
    }

}
