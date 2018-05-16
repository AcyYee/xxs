package com.cwy.xxs.vo;

import lombok.Data;

import java.util.List;

/**
 * @author acy19
 */
@Data
public class OrderSubmit {

    private Integer specificationId,itemType,orderCategory,itemCount;

    private String orderRemark,personId,storeId,groupId,buyId,addressInfo;

    private Integer addressType;

    private List<Integer> itemIds;

}
