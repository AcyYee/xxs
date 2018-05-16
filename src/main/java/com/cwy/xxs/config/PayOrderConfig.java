package com.cwy.xxs.config;

/**
 * @author acy19
 */
public class PayOrderConfig {

    public static final String PAY_NOTIFY ="https://mmp.mtree.cn/xxs/wxPay/payBack";

    public static final String REFUND_NOTIFY ="https://mmp.mtree.cn/xxs/wxPay/refundBack";

    /**
     * 标识开启 是 true
     */
    public static final int COMMON_IS = 1;

    /**
     * 标识关闭 否 false
     */
    public static final int COMMON_NOT = 0;

    /**
     * 订单失效
     */
    public static final int ORDER_NOT_ALLOW = 0;

    /**
     * 订单未支付
     */
    public static final int ORDER_UN_PAY = 1;

    /**
     * 订单支付完成等待配送
     */
    public static final int ORDER_PAY = 2;

    /**
     * 订单正在配送
     */
    public static final int ORDER_IN_SEND = 3;

    /**
     * 订单配送完成 未评价
     */
    public static final int ORDER_COMPLETE = 4;

    /**
     * 订单退款中
     */
    public static final int ORDER_IN_REFUSE = 5;

    /**
     * 订单退款完成
     */
    public static final int ORDER_COM_REFUSE = 6;

    /**
     * 订单已评价
     */
    public static final int ORDER_COMMENT_COMPLETE = 7;

    /**
     * 团购订单 在团
     */
    public static final int ORDER_GROUP_IN = 8;

    /**
     * 团购发起
     */
    public static final int GROUP_INFO_CREATE = 1;

    /**
     * 团购开启
     */
    public static final int GROUP_INFO_START = 2;

    /**
     * 团购完成
     */
    public static final int GROUP_INFO_COMPLETE = 3;

    /**
     * 团购关闭
     */
    public static final int GROUP_INFO_DED = 0;

    /**
     * 拼团关闭
     */
    public static final int GROUP_ITEM_NOT = 0;

    /**
     * 拼团开启
     */
    public static final int GROUP_ITEM_IN = 1;

    /**
     * 拼团完成
     */
    public static final int GROUP_ITEM_COMPLETE = 2;


    /**
     * 购物车订单项
     */
    public static final int ITEM_SHOP_CAR =1 ;

    /**
     * 订单订单项
     */
    public static final int ITEM_ORDER =2 ;

    /**
     * 单个订单
     */
    public static final int ORDER_ONE_COMMON =1;

    /**
     * 购物车订单
     */
    public static final int ORDER_SHOP_CAR =2;

    /**'
     * 开启团购订单
     */
    public static final int ORDER_GROUP_OPEN =3;

    /**
     * 参与团购订单
     */
    public static final int ORDER_GROUP_JOIN =4;

    /**
     * 配送订单
     */
    public static final int ADDRESS_SEND = 1;

    /**
     * 自取订单
     */
    public static final int ADDRESS_SELF = 2;

    /**
     * 微信地址订单
     */
    public static final int ADDRESS_WX = 3;

}
