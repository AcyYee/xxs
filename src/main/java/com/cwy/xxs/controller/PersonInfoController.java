package com.cwy.xxs.controller;

import com.cwy.xxs.service.PersonInfoService;
import com.cwy.xxs.vo.FindModel;
import com.cwy.xxs.vo.PersonVO;
import com.cwy.xxs.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

import static com.cwy.xxs.util.HttpSend.getRemoteIP;

/**
 * @author acy19
 */
@RestController
@RequestMapping("personInfo")
public class PersonInfoController {

    private final PersonInfoService personInfoService;

    @Autowired
    public PersonInfoController(PersonInfoService personInfoService) {
        this.personInfoService = personInfoService;
    }

    @PostMapping("wx/login")
    public ResultData login(@RequestBody PersonVO person, HttpServletRequest request){
        String ip = getRemoteIP(request);
        return personInfoService.wxLogin(person.getCode(),ip);
    }

    @PostMapping("wx/find")
    public ResultData wxFind(@RequestBody FindModel findModel){
        return personInfoService.wxfind(findModel.getId());
    }

    @PostMapping("find")
    public ResultData find(@RequestBody FindModel findModel, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        if (findModel == null || findModel.getId() == null){
            return ResultData.returnResultData(-1,null);
        }
        return personInfoService.find(findModel.getId());
    }

    @PostMapping("finds")
    public ResultData finds(@RequestBody Map<String,Object> map, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return personInfoService.getVips(map);
    }

    @PostMapping("wx/registerVIP")
    public ResultData registerVIP(@RequestBody Map<String,String> map,HttpServletRequest request){
        String ip = getRemoteIP(request);
        String id = map.get("id");
        String userPhone = map.get("userPhone");
        String phoneCode = map.get("phoneCode");
        String realName = map.get("realName");
        String userAddress = map.get("userAddress");
        String formId = map.get("formId");
        return personInfoService.registerVIP(id,userPhone,phoneCode,realName,userAddress,formId,ip);
    }

    @PostMapping("wx/setUserInfo")
    public ResultData setUserInfo(@RequestBody Map<String,String> map,HttpServletRequest request){
        String id = map.get("id");
        String wxNico = map.get("wxNico");
        String wxIcon = map.get("wxIcon");
        String wxGender = map.get("wxGender");
        String wxProvince = map.get("wxProvince");
        String wxCity = map.get("wxCity");
        String wxCountry = map.get("wxCountry");
        String ip = getRemoteIP(request);
        return personInfoService.setUserInfo(id,wxNico,wxIcon,wxGender,wxProvince,wxCity,wxCountry,ip);
    }

}
