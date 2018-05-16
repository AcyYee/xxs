package com.cwy.xxs.controller;

import com.cwy.xxs.entity.UserShopCar;
import com.cwy.xxs.service.UserShopCarService;
import com.cwy.xxs.vo.FindModel;
import com.cwy.xxs.vo.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("userShopCar")
public class UserShopCarController {

    private final UserShopCarService userShopCarService;

    public UserShopCarController(UserShopCarService userShopCarService) {
        this.userShopCarService = userShopCarService;
    }

    @PostMapping("find")
    public ResultData find(@RequestBody FindModel findModel){
        return userShopCarService.findUserShopCar(findModel);
    }

}
