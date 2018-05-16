package com.cwy.xxs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "user_shop_car_acy")
public class UserShopCar {

    @Id
    private String id;

    private String personId,createTime,updateTime;

}