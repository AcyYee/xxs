package com.cwy.xxs.vo;

import com.cwy.xxs.entity.OrderInfo;
import lombok.Data;

import java.util.List;

/**
 * @author acy 屋大维
 */
@Data
public class OrderInfoViewModel {

    private OrderInfo orderInfo;

    private List<OrderItemModel> orderItemModels;


}
