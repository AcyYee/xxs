package com.cwy.xxs.service.impl;

import com.cwy.xxs.dao.mongo.base.GroupBuyBaseDao;
import com.cwy.xxs.dao.mongo.base.GroupInfoBaseDao;
import com.cwy.xxs.dao.mongo.base.GroupItemBaseDao;
import com.cwy.xxs.dao.mongo.plus.GroupInfoDao;
import com.cwy.xxs.dao.mongo.plus.GroupItemDao;
import com.cwy.xxs.dao.mybatis.CommoditySpecificationMapper;
import com.cwy.xxs.dao.mybatis.OrderInfoMapper;
import com.cwy.xxs.dao.mybatis.OrderItemMapper;
import com.cwy.xxs.entity.GroupInfo;
import com.cwy.xxs.entity.GroupItem;
import com.cwy.xxs.entity.OrderInfo;
import com.cwy.xxs.entity.OrderItem;
import com.cwy.xxs.service.GroupItemService;
import com.cwy.xxs.vo.GroupInfoModel;
import com.cwy.xxs.vo.PageData;
import com.cwy.xxs.vo.ResultData;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author acy19
 */
@Service
public class GroupItemServiceImpl implements GroupItemService {

    private final GroupInfoBaseDao groupInfoBaseDao;

    private final GroupBuyBaseDao groupBuyBaseDao;

    private final GroupItemBaseDao groupItemBaseDao;

    private final GroupInfoDao groupInfoDao;

    private final GroupItemDao groupItemDao;

    private final CommoditySpecificationMapper commoditySpecificationMapper;

    private final OrderItemMapper orderItemMapper;

    private final OrderInfoMapper orderInfoMapper;

    public GroupItemServiceImpl(
            GroupInfoBaseDao groupInfoBaseDao,
            GroupBuyBaseDao groupBuyBaseDao, GroupItemBaseDao groupItemBaseDao,
            GroupInfoDao groupInfoDao, GroupItemDao groupItemDao, CommoditySpecificationMapper commoditySpecificationMapper,
            OrderItemMapper orderItemMapper, OrderInfoMapper orderInfoMapper) {
        this.groupInfoBaseDao = groupInfoBaseDao;
        this.groupBuyBaseDao = groupBuyBaseDao;
        this.groupItemBaseDao = groupItemBaseDao;
        this.groupInfoDao = groupInfoDao;
        this.groupItemDao = groupItemDao;
        this.commoditySpecificationMapper = commoditySpecificationMapper;
        this.orderItemMapper = orderItemMapper;
        this.orderInfoMapper = orderInfoMapper;
    }

    @Override
    public ResultData findGroupItems(Map<String, Object> map) {
        String groupId = (String) map.get("groupId");
        if (groupId == null){
            return ResultData.returnResultData(-1);
        }
        PageData pageData = new PageData();
        pageData.setModelData(groupItemBaseDao.findByGroupId(groupId));
        return ResultData.returnResultData(1,pageData);
    }

    @Override
    public ResultData findGroupItemORs(Map<String, Object> map) {
        String groupId = (String) map.get("groupId");
        if (groupId == null){
            return ResultData.returnResultData(-1);
        }
        PageData pageData = new PageData();
        Integer pageIndex,pageSize;
        pageIndex = (Integer) map.get("pageIndex");
        pageSize = (Integer) map.get("pageSize");
        List<GroupInfoModel> groupInfoModels = new ArrayList<>(15);
        List<GroupItem> groupItems;
        if (pageIndex != null) {
            Pageable pageable;
            Sort sort = new Sort(Sort.Direction.DESC,"id");
            if (pageSize == null) {
                pageable = new PageRequest(pageIndex, 15, sort);

            }else{
                pageable = new PageRequest(pageIndex, pageSize, sort);
            }
            map.put("pageable",pageable);
            pageData.setPageable(pageable);
            groupItems = groupItemDao.findGroupItems(map);
        }else {
            groupItems = groupItemDao.findGroupItems(map);
        }
        for (GroupItem groupItem : groupItems){
            GroupInfoModel groupInfoModel = new GroupInfoModel();
            groupInfoModel.setGroupItem(groupItem);
            GroupInfo groupInfo = groupInfoBaseDao.findOne(groupItem.getGroupId());
            groupInfoModel.setGroupInfo(groupInfo);
            OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(groupItem.getOrderId());
            groupInfoModel.setGroupBuy(groupBuyBaseDao.findOne(groupInfo.getBuyId()));
            OrderItem orderItem = orderItemMapper.findByOrderId(orderInfo.getOrderId());
            groupInfoModel.setOrderItem(orderItem);
            groupInfoModel.setCommoditySpecification(commoditySpecificationMapper.selectByPrimaryKey(orderItem.getSpecificationId()));
            groupInfoModel.setOrderInfo(orderInfo);
            groupInfoModels.add(groupInfoModel);
        }
        pageData.setModelData(groupInfoModels);
        return ResultData.returnResultData(1,pageData);
    }

}
