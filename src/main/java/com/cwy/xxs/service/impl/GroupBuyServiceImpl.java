package com.cwy.xxs.service.impl;

import com.cwy.xxs.dao.mongo.base.GroupBuyBaseDao;
import com.cwy.xxs.dao.mongo.plus.CommodityInfoDao;
import com.cwy.xxs.dao.mongo.plus.GroupBuyDao;
import com.cwy.xxs.entity.GroupBuy;
import com.cwy.xxs.service.GroupBuyService;
import com.cwy.xxs.util.TimeUtil;
import com.cwy.xxs.vo.FindModel;
import com.cwy.xxs.vo.Operate;
import com.cwy.xxs.vo.PageData;
import com.cwy.xxs.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author acy19
 */
@Service("groupBuyService")
public class GroupBuyServiceImpl implements GroupBuyService {

    private final GroupBuyDao groupBuyDao;

    private final CommodityInfoDao commodityInfoDao;

    private final GroupBuyBaseDao groupBuyBaseDao;

    @Autowired
    public GroupBuyServiceImpl(GroupBuyDao groupBuyDao, CommodityInfoDao commodityInfoDao, GroupBuyBaseDao groupBuyBaseDao) {
        this.groupBuyDao = groupBuyDao;
        this.commodityInfoDao = commodityInfoDao;
        this.groupBuyBaseDao = groupBuyBaseDao;
    }

    @Override
    public ResultData addGroupBuy(GroupBuy groupBuy) {
        if (groupBuy == null || groupBuy.empty()){
            return ResultData.returnResultData(-1,groupBuy);
        }
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        groupBuy.setCreateTime(dateTime);
        groupBuy.setUpdateTime(dateTime);
        groupBuy.setIsDelete(0);
        if (groupBuy.getIsUsed() == null){
            groupBuy.setIsUsed(1);
        }
        if (groupBuy.getBuyType() == null){
            groupBuy.setBuyType(1);
        }
        commodityInfoDao.updateGroup(groupBuy.getCommodityId());
        groupBuy = groupBuyBaseDao.save(groupBuy);
        if (groupBuy == null || groupBuy.getId() == null || "".equals(groupBuy.getId())){
            return ResultData.returnResultData(0,groupBuy);
        }
        return ResultData.returnResultData(1,groupBuy);
    }

    @Override
    public ResultData updateGroupBuy(GroupBuy groupBuy) {
        if (groupBuy == null || groupBuy.notEmpty()){
            return ResultData.returnResultData(-1,groupBuy);
        }
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        groupBuy.setUpdateTime(dateTime);
        return ResultData.returnResultData(groupBuyDao.update(groupBuy),groupBuyBaseDao.findOne(groupBuy.getId()));
    }

    @Override
    public ResultData deleteGroupBuies(Operate operate) {
        if (operate == null || operate.getIds() == null || operate.getIds().size()<1){
            return ResultData.returnResultData(-1,operate);
        }
        Operate operate1 =new Operate();
        operate1.setIds(operate.getIds());
        operate1.setType(0);
        useGroupBuies(operate1);
        return ResultData.returnResultData(groupBuyDao.deleteBuIds(operate.getIds()),operate.getIds());
    }

    @Override
    public ResultData useGroupBuies(Operate operate) {
        if (operate == null || operate.getIds() == null || operate.getIds().size()<1 || operate.getType() == null){
            return ResultData.returnResultData(-1,operate);
        }
        if (operate.getType() == 0){
            commodityInfoDao.updateGroups(operate.getIds(),-1);
        }else {
            commodityInfoDao.updateGroups(operate.getIds(),1);
        }
        return ResultData.returnResultData(groupBuyDao.useBuIds(operate),operate.getIds());
    }

    @Override
    public ResultData findGroupBuy(FindModel findModel) {
        if (findModel == null || findModel.getId() == null || "".equals(findModel.getId())){
            return ResultData.returnResultData(-1,findModel);
        }
        GroupBuy groupBuy = groupBuyBaseDao.findOne(findModel.getId());
        if (groupBuy == null){
            return ResultData.returnResultData(-1);
        }else{
            return ResultData.returnResultData(1,groupBuy);
        }
    }

    @Override
    public ResultData findGroupBuies(Map<String, Object> map) {
        Integer pageIndex = (Integer) map.get("pageIndex");
        Integer pageSize = (Integer) map.get("pageSize");
        PageData pageData = new PageData();
        if (pageIndex != null) {
            Pageable pageable;
            if (pageSize == null) {
                pageable = new PageRequest(pageIndex, 15);
            }else{
                pageable = new PageRequest(pageIndex, pageSize);
            }
            map.put("pageable",pageable);
            pageData.setPageable(pageable);
            pageData.setModelData(groupBuyDao.selectGroupBuies(map));
        }else {
            pageData.setModelData(groupBuyDao.selectGroupBuies(map));
        }
        return  ResultData.returnResultData(1,pageData);
    }

}
