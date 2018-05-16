package com.cwy.xxs.dao.mybatis;

import com.cwy.xxs.entity.OrderInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author acy 屋大维
 */
public interface OrderInfoMapper {

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateTypeById(@Param("orderId") String orderId,@Param("updateTime") String updateTime, @Param("orderType") int orderType);

    int findInfoCount(Map<String, Object> map);

    int updatePayById(@Param("allowPrice") Double allowPrice,@Param("dateTime") String dateTime,@Param("orderId") String orderId);

    List<OrderInfo> findINFOS(Map<String, Object> map);

    int deleteByIds(@Param("orderIds") List orderIds);

    void updateTypeByGroupId(@Param("orderType") int orderType,@Param("updateTime") String updateTime,@Param("groupId") String groupId);

    OrderInfo selectByGroupIdAndPerson(@Param("groupId") String groupId,@Param("personId") String personId);

    List<OrderInfo> findGroupOrders(Map<String, Object> map);

    int findGroupOrderCount(Map<String, Object> map);
}