package com.cwy.xxs.service;

import com.cwy.xxs.entity.OrderItem;
import com.cwy.xxs.vo.Operate;
import com.cwy.xxs.vo.PageData;
import com.cwy.xxs.vo.ResultData;

import java.util.Map;

public interface OrderItemService {

    ResultData addOrderItem(OrderItem orderItem);

    int updateOrderItem(OrderItem orderItem);

    int deleteOrderItems(Operate operate);

    OrderItem findOrderItem(Integer itemId);

    PageData findOrderItems(Map<String, Object> map);
}
