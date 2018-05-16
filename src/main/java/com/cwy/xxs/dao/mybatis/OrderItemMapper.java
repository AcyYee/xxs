package com.cwy.xxs.dao.mybatis;

import com.cwy.xxs.entity.OrderItem;
import com.cwy.xxs.vo.OrderItemModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderItemMapper {

    int insert(OrderItem record);

    OrderItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateCountByPrimaryKey(OrderItem record);

    int deleteByIds(@Param("itemIds") List itemIds);

    int findItemCounts(Map<String, Object> map);

    List<OrderItemModel> findItems(Map<String, Object> map);

    OrderItem findBySpecificationAndShopCar(@Param("specificationId") Integer specificationId, @Param("shopCarId") String shopCarId);

    /**
     * 仅限单个购买或拼团
     * @param orderId
     * @return
     */
    OrderItem findByOrderId(String orderId);
}