package com.cwy.xxs.entity;

import lombok.Data;

/**
 * @author acy 屋大维
 */
@Data
public class OrderItem {

    private Integer itemId;

    private Double itemPrice;

    private Integer specificationId;

    private String orderId;

    private String shopCarId;

    private Integer itemCount;

    private Integer itemWeight;

    private Integer itemType;

    private String createTime;

    private String updateTime;

    public boolean notEmpty() {
        return itemCount == null || specificationId == null || shopCarId == null;
    }

    public boolean empty() {
        return itemCount == null && specificationId == null;
    }
}