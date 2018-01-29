package com.cwy.xxs.controller;

import com.cwy.xxs.service.PersonInfoService;
import com.cwy.xxs.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author acy19
 */
@RestController
@RequestMapping("personInfo")
public class PersonInfoController {

    @Autowired
    private PersonInfoService personInfoService;

    @PostMapping("login")
    public ResultData login(String code){
        return personInfoService.wxLogin(code);
    }

}
