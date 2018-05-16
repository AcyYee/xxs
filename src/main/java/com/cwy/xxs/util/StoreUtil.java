package com.cwy.xxs.util;

import com.cwy.xxs.entity.*;
import com.cwy.xxs.vo.ResultData;

import static com.cwy.xxs.config.PayOrderConfig.*;

public class StoreUtil {

    private static final String STORE_ADDRESS = "浙江省杭州市下城区建国北路194号可的便利店";

    private static final String COMMUNITY_ADDRESS = "浙江省杭州市下城区潮鸣寺巷红枫苑";

    public static String generateAddress(Integer type, String phone, String name, String address) {
        switch (type) {
            case ADDRESS_SEND:
                return "{\"phone\":\"" + phone + "\",\"name\":\"" + name + "\",\"address\":\"" + COMMUNITY_ADDRESS + address + "\"}";
            case ADDRESS_SELF:
                return "{\"phone\":\"" + phone + "\",\"name\":\"" + name + "\",\"address\":\"" + STORE_ADDRESS + "\"}";
            case ADDRESS_WX:
                return "{\"phone\":\"" + phone + "\",\"name\":\"" + name + "\",\"address\":\"" + address + "\"}";
            default:
                return "{\"phone\":\"" + phone + "\",\"name\":\"" + name + "\",\"address\":\"" + COMMUNITY_ADDRESS + address + "\"}";
        }
    }

    /**
     * 计算订单项重量和价格
     * @param orderItem 订单项信息
     * @param commoditySpecification 商品规格信息
     * @param commodityInfo 商品信息
     */
    public static void setOrderPrice(OrderItem orderItem, CommoditySpecification commoditySpecification, CommodityInfo commodityInfo){
        if (commodityInfo.getIsActivity() == COMMON_IS){
            orderItem.setItemPrice(orderItem.getItemCount()*commoditySpecification.getActivityPrice());
        }else {
            orderItem.setItemPrice(orderItem.getItemCount()*commoditySpecification.getSpecificationPrice());
        }
        if (commodityInfo.getIsDiscount() == COMMON_IS){
            orderItem.setItemPrice(orderItem.getItemPrice()*commodityInfo.getCommodityDiscount());
        }
        orderItem.setItemWeight(commoditySpecification.getSpecificationWeight() * orderItem.getItemCount());
    }

    public static void updatePersonVipGrade(OrderInfo orderInfo, PersonInfo personInfo) {
        if (orderInfo ==  null){
            return ;
        }
        if (personInfo.getVipGrade() == null){
            personInfo.setVipGrade((int) Math.round(orderInfo.getPayPrice()));
        }else {
            personInfo.setVipGrade(personInfo.getVipGrade() + (int) Math.round(orderInfo.getPayPrice()));
        }
    }

    public static void main(String[] args) {
        //测试完成 3种类型
        System.out.println(generateAddress(3,"17858959437","叶文雄","3幢"));
    }


}
