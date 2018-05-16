package com.cwy.xxs.vo;

import com.cwy.xxs.entity.*;
import lombok.Data;

import java.util.List;

@Data
public class GroupInfoModel {

    private GroupItem groupItem;

    private GroupInfo groupInfo;

    private GroupBuy groupBuy;

    private CommoditySpecification commoditySpecification;

    private OrderItem orderItem;

    private OrderInfo orderInfo;

}
