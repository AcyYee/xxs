package com.cwy.xxs.service.impl;

import com.cwy.xxs.dao.mongo.base.CommodityInfoBaseDao;
import com.cwy.xxs.dao.mybatis.CommoditySpecificationMapper;
import com.cwy.xxs.dao.mybatis.OrderItemMapper;
import com.cwy.xxs.entity.CommodityInfo;
import com.cwy.xxs.entity.CommoditySpecification;
import com.cwy.xxs.entity.OrderItem;
import com.cwy.xxs.service.OrderItemService;
import com.cwy.xxs.util.TimeUtil;
import com.cwy.xxs.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cwy.xxs.config.PayOrderConfig.COMMON_IS;
import static com.cwy.xxs.config.PayOrderConfig.ITEM_SHOP_CAR;
import static com.cwy.xxs.util.StoreUtil.setOrderPrice;

@Service("orderItemService")
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemMapper orderItemDao;

    private final CommoditySpecificationMapper commoditySpecificationMapper;

    private final CommodityInfoBaseDao commodityInfoBaseDao;

    @Autowired
    public OrderItemServiceImpl(OrderItemMapper orderItemDao, CommoditySpecificationMapper commoditySpecificationMapper, CommodityInfoBaseDao commodityInfoBaseDao) {
        this.orderItemDao = orderItemDao;
        this.commoditySpecificationMapper = commoditySpecificationMapper;
        this.commodityInfoBaseDao = commodityInfoBaseDao;
    }

    @Override
    public ResultData addOrderItem(OrderItem orderItem) {
        if (orderItem == null || orderItem.notEmpty()){
            return ResultData.returnResultData(-1);
        }
        OrderItem temp = orderItemDao.findBySpecificationAndShopCar(orderItem.getSpecificationId(),orderItem.getShopCarId());
        CommoditySpecification commoditySpecification = commoditySpecificationMapper.selectByPrimaryKey(orderItem.getSpecificationId());
        CommodityInfo commodityInfo = commodityInfoBaseDao.findOne(commoditySpecification.getCommodityId());
        return generateOrChangeShopCarItem(orderItem,temp,commoditySpecification,commodityInfo);
    }

    /**
     * 生成或修改购物车中的订单项
     * @param orderItem 提交数据
     * @param temp 以后订单项
     * @param commoditySpecification 商品规格信息
     * @param commodityInfo 商品信息
     * @return 返回订单项
     */
    private ResultData generateOrChangeShopCarItem(OrderItem orderItem,OrderItem temp,CommoditySpecification commoditySpecification,CommodityInfo commodityInfo){
        if (temp == null){
            orderItem.setCreateTime(TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC));
            orderItem.setItemType(ITEM_SHOP_CAR);
            setOrderPrice(orderItem, commoditySpecification, commodityInfo);
            return ResultData.returnResultData(orderItemDao.insert(orderItem),orderItem);
        }else {
            temp.setItemCount(orderItem.getItemCount()+temp.getItemCount());
            setOrderPrice(temp, commoditySpecification, commodityInfo);
            orderItem = temp;
            return ResultData.returnResultData(orderItemDao.updateByPrimaryKeySelective(orderItem),orderItem);
        }
    }



    @Override
    public int updateOrderItem(OrderItem orderItem) {
        if (orderItem == null || orderItem.getItemId() == null || orderItem.getItemCount() == null ||orderItem.getItemCount()<1){
            return -1;
        }
        CommoditySpecification commoditySpecification = commoditySpecificationMapper.selectByPrimaryKey(orderItem.getSpecificationId());
        CommodityInfo commodityInfo = commodityInfoBaseDao.findOne(commoditySpecification.getCommodityId());
        setOrderPrice(orderItem,commoditySpecification,commodityInfo);
        return orderItemDao.updateCountByPrimaryKey(orderItem);
    }

    @Override
    public int deleteOrderItems(Operate operate) {
        if (operate == null){
            return -1;
        }
        List itemIds = operate.getIds();
        if(itemIds == null || itemIds.size() <1){
            return -1;
        }
        return orderItemDao.deleteByIds(itemIds);
    }

    @Override
    public PageData findOrderItems(Map<String,Object> map) {
        String orderId = (String) map.get("orderId");
        String shopCarId = (String) map.get("shopCarId");
        if(orderId == null && shopCarId == null) {
            return null;
        }
        PageData pageData = new PageData();
        Integer pageIndex = (Integer) map.get("pageIndex");
        Integer pageSize = (Integer) map.get("pageSize");
        if (pageIndex != null){
            PageModel pageModel = new PageModel();
            pageModel.setPageIndex(pageIndex);
            if (pageSize == null){
                pageModel.setPageSize(15);
            }else {
                pageModel.setPageSize(pageSize);
            }
            pageModel.setRecordCount(orderItemDao.findItemCounts(map));
            pageData.setPageModel(pageModel);
        }
        List<OrderItemModel> models =orderItemDao.findItems(map);
        // TODO 牺牲性能但是没有办法 后期改进
        for (OrderItemModel orderItemModel: models){
            orderItemModel.setCommodityName(commodityInfoBaseDao.findOne(orderItemModel.getCommodityId()).getCommodityName());
        }
        pageData.setModelData(models);
        return pageData;
    }

    @Override
    public OrderItem findOrderItem(Integer itemId) {
        if (itemId == null || itemId <1) {
            return null;
        }
        else {
            return orderItemDao.selectByPrimaryKey(itemId);
        }
    }

}
