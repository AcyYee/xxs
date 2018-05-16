package com.cwy.xxs.vo;

import lombok.Data;

@Data
public class OrderItemModel {

    private Integer itemId;

    private Integer itemCount;

    private Double itemPrice;

    private String specificationName;

    private Integer specificationId;

    private Double specificationPrice;

    private String imagePath;

    private Integer isDiscount;

    private String commodityName;

    private String commodityId;

}
