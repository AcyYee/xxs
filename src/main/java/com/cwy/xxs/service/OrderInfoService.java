package com.cwy.xxs.service;

import com.cwy.xxs.entity.OrderInfo;
import com.cwy.xxs.vo.*;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author acy 屋大维
 */

public interface OrderInfoService {

    /**
     *  发起单个物品支付请求 包括 发起参与团购
     * @param orderSubmit 订单提交信息
     * @param ip ip
     * @return 返回支付信息
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ,rollbackFor = Exception.class)
    ResultData addOneItem(OrderSubmit orderSubmit, String ip);

    /**
     *  发起单个物品支付请求 包括 发起参与团购
     * @param orderSubmit 订单提交信息
     * @param ip ip
     * @return 返回支付信息
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ,rollbackFor = Exception.class)
    ResultData addAllItem(OrderSubmit orderSubmit, String ip);

    /**
     *
     * @param orderId
     * @return
     */
    OrderInfo findInfo(String orderId);

    PageData findGroupOrders(Map<String, Object> map);

    /**
     *
     * @param orderId
     * @return
     */
    OrderInfoViewModel findInfoModel(String orderId);

    /**
     *
     * @param orderId
     * @param ip
     * @return
     */
    ResultData wxPay(String orderId, String ip);

    /**
     *
     * @param operate
     * @return
     */
    int userDeletes(Operate operate);

    /**
     *
     * @param map
     * @return
     */
    int payManual(Map<String,Object> map);

    /**
     *
     * @param xmlString
     * @return
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ,rollbackFor = Exception.class)
    String wxPayBack(String xmlString);

    ResultData cancel(FindModel findModel);

    /**
     *
     * @param findModel
     * @return
     */
    ResultData send(FindModel findModel);

    /**
     *
     * @param findModel
     * @return
     */
    ResultData receive(FindModel findModel);

    /**
     *
     * @param map
     * @return
     */
    PageData findInfoes(Map<String,Object> map);

    @Transactional(isolation = Isolation.REPEATABLE_READ,rollbackFor = Exception.class)
    String wxRefundBack(String xmlString);

}
