package com.cwy.xxs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "order_info_acy")
@Data
public class OrderInfo {

    private String prepayId;

    private String orderId;

    private String groupId;

    private String personId;

    private Integer addressType;

    private Double allPrice;

    private Double allowPrice,mailPrice;

    private Double payPrice;

    private String createTime;

    private String updateTime;

    private String payTime;

    private Integer orderType,orderCategory;

    private String orderRemark;

    private String addressInfo,phoneNumber;

    private String storeId;

    private Integer isDelete;

}