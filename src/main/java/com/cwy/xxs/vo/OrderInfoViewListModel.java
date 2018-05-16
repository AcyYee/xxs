package com.cwy.xxs.vo;

import com.cwy.xxs.entity.OrderInfo;
import lombok.Data;

import java.util.List;

@Data
public class OrderInfoViewListModel {

    private OrderInfo orderInfo;

    private List<OrderItemModel> orderItems;

    public OrderInfoViewListModel(OrderInfo orderInfo, List<OrderItemModel> items) {
        this.orderInfo = orderInfo;
        this.orderItems = items;
    }

}
