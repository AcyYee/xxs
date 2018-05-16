package com.cwy.xxs.service.impl;

import com.cwy.xxs.dao.mongo.base.GroupBuyBaseDao;
import com.cwy.xxs.dao.mongo.base.GroupInfoBaseDao;
import com.cwy.xxs.dao.mongo.base.GroupItemBaseDao;
import com.cwy.xxs.dao.mongo.plus.GroupInfoDao;
import com.cwy.xxs.dao.mybatis.CommoditySpecificationMapper;
import com.cwy.xxs.dao.mybatis.OrderInfoMapper;
import com.cwy.xxs.dao.mybatis.OrderItemMapper;
import com.cwy.xxs.entity.GroupInfo;
import com.cwy.xxs.entity.OrderInfo;
import com.cwy.xxs.entity.OrderItem;
import com.cwy.xxs.service.GroupInfoService;
import com.cwy.xxs.vo.*;
import com.sun.tools.corba.se.idl.constExpr.Or;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GroupInfoServiceImpl implements GroupInfoService {

    private final GroupInfoBaseDao groupInfoBaseDao;

    private final GroupBuyBaseDao groupBuyBaseDao;

    private final GroupItemBaseDao groupItemBaseDao;

    private final GroupInfoDao groupInfoDao;

    private final CommoditySpecificationMapper commoditySpecificationMapper;

    private final OrderItemMapper orderItemMapper;

    private final OrderInfoMapper orderInfoMapper;

    private final Logger logger = LoggerFactory.getLogger(GroupInfoServiceImpl.class);

    @Autowired
    public GroupInfoServiceImpl(GroupInfoBaseDao groupInfoBaseDao, GroupInfoDao groupInfoDao, GroupBuyBaseDao groupBuyBaseDao, GroupItemBaseDao groupItemBaseDao, CommoditySpecificationMapper commoditySpecificationMapper, OrderItemMapper orderItemMapper, OrderInfoMapper orderInfoMapper) {
        this.groupInfoBaseDao = groupInfoBaseDao;
        this.groupInfoDao = groupInfoDao;
        this.groupBuyBaseDao = groupBuyBaseDao;
        this.groupItemBaseDao = groupItemBaseDao;
        this.commoditySpecificationMapper = commoditySpecificationMapper;
        this.orderItemMapper = orderItemMapper;
        this.orderInfoMapper = orderInfoMapper;
    }

    @Override
    public ResultData findGroupInfo(FindModel findModel) {
        if (findModel == null || findModel.getId()==null){
            return ResultData.returnResultData(-1);
        }
        GroupInfo groupInfo = groupInfoBaseDao.findOne(findModel.getId());
        if (groupInfo == null){
            return ResultData.returnResultData(-1);
        }
        return ResultData.returnResultData(1,groupInfo);
    }

    @Override
    public ResultData findGroupInfoes(Map<String, Object> map) {
        PageData pageData = new PageData();
        Integer pageIndex,pageSize;
        pageIndex = (Integer) map.get("pageIndex");
        pageSize = (Integer) map.get("pageSize");
        List<GroupInfoModel> groupInfoModels = new ArrayList<>(15);
        List<GroupInfo> groupInfos;
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
            groupInfos = groupInfoDao.findGroupInfoes(map);
        }else {
            groupInfos = groupInfoDao.findGroupInfoes(map);
        }
        for (GroupInfo groupInfo : groupInfos){
            GroupInfoModel groupInfoModel = new GroupInfoModel();
            OrderInfo orderInfo;
            groupInfoModel.setGroupInfo(groupInfo);
            orderInfo = orderInfoMapper.selectByGroupIdAndPerson(groupInfo.getId(),groupInfo.getPersonId());
            if (orderInfo == null){
                continue;
            }
            groupInfoModel.setGroupBuy(groupBuyBaseDao.findOne(groupInfo.getBuyId()));
            OrderItem orderItem = orderItemMapper.findByOrderId(orderInfo.getOrderId());
            if (orderItem == null){
                continue;
            }
            groupInfoModel.setOrderItem(orderItem);
            groupInfoModel.setCommoditySpecification(commoditySpecificationMapper.selectByPrimaryKey(orderItem.getSpecificationId()));
            groupInfoModel.setOrderInfo(orderInfo);
            groupInfoModels.add(groupInfoModel);
        }
        pageData.setModelData(groupInfoModels);
        return ResultData.returnResultData(1,pageData);
    }

    @Override
    public ResultData deleteGroupInfoes(Operate operate) {
        List ids = null;
        if (operate != null){
            ids = operate.getIds();
        }
        if (ids == null ||ids.size()<1 ) {
            return ResultData.returnResultData(-1,null);
        }else {
            return ResultData.returnResultData(groupInfoDao.deleteByIds(ids));
        }
    }

    @Override
    public ResultData findGroupInfoModel(FindModel findModel) {
        if (findModel == null || findModel.getId()==null){
            return ResultData.returnResultData(-1);
        }
        GroupModel groupModel = new GroupModel();
        GroupInfo groupInfo = groupInfoBaseDao.findOne(findModel.getId());
        if (groupInfo == null){
            return ResultData.returnResultData(-1);
        }
        groupModel.setGroupInfo(groupInfo);
        groupModel.setGroupBuy(groupBuyBaseDao.findOne(groupInfo.getBuyId()));
        groupModel.setGroupItems(groupItemBaseDao.findByGroupId(groupInfo.getId()));
        return ResultData.returnResultData(1,groupModel);
    }
}
