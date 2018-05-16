package com.cwy.xxs.service.impl;

import com.cwy.xxs.dao.mongo.base.CommodityInfoBaseDao;
import com.cwy.xxs.dao.mongo.plus.CommodityInfoDao;
import com.cwy.xxs.entity.CommodityInfo;
import com.cwy.xxs.service.CommodityInfoService;
import com.cwy.xxs.util.TimeUtil;
import com.cwy.xxs.vo.FindModel;
import com.cwy.xxs.vo.Operate;
import com.cwy.xxs.vo.PageData;
import com.cwy.xxs.vo.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author acy19
 */
@Service("commodityInfoService")
public class CommodityInfoServiceImpl implements CommodityInfoService {

    private final Logger logger = LoggerFactory.getLogger(CommodityInfoServiceImpl.class);

    private final CommodityInfoBaseDao commodityInfoBaseDao;

    private final CommodityInfoDao commodityInfoDao;

    @Autowired
    public CommodityInfoServiceImpl(CommodityInfoBaseDao commodityInfoBaseDao,CommodityInfoDao commodityInfoDao) {
        this.commodityInfoBaseDao = commodityInfoBaseDao;
        this.commodityInfoDao = commodityInfoDao;
    }

    @Override
    public ResultData addCommodityInfo(CommodityInfo commodityInfo) {
        System.out.println(commodityInfo);
        if (commodityInfo == null || commodityInfo.empty()) {
            return ResultData.returnResultData(-1,null);
        }
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        commodityInfo.setCreateTime(dateTime);
        commodityInfo.setUpdateTime(dateTime);
        commodityInfo.setCommodityType(1);
        commodityInfo.setIsActivity(0);
        commodityInfo.setIsDelete(0);
        commodityInfo.setIsDiscount(0);
        commodityInfo.setIsGroup(0);
        if (commodityInfo.getIsSales() == null) {
            commodityInfo.setIsSales(1);
        }
        commodityInfo = commodityInfoBaseDao.save(commodityInfo);
        if (commodityInfo != null){
            return ResultData.returnResultData(1,commodityInfo);
        }else {
            return ResultData.returnResultData(0,null);
        }
    }

    @Override
    public ResultData updateCommodityInfo(CommodityInfo commodityInfo) {
        if (commodityInfo == null || commodityInfo.getId() == null){
            return ResultData.returnResultData(-1,null);
        }
        return ResultData.returnResultData(commodityInfoDao.update(commodityInfo),commodityInfoBaseDao.findOne(commodityInfo.getId()));
    }

    @Override
    public ResultData deletesCommodityInfo(Operate operate) {
        List ids = null;
        if (operate != null){
            ids = operate.getIds();
        }
        if (ids == null || ids.size()==0) {
            return ResultData.returnResultData(-1,null);
        } else {
            return ResultData.returnResultData(commodityInfoDao.deleteByIds(ids),null);
        }
    }

    @Override
    public ResultData findById(FindModel findModel) {
        if (findModel == null || findModel.getId() == null){
            return ResultData.returnResultData(-1,null);
        }else {
            CommodityInfo commodityInfo = commodityInfoBaseDao.findOne(findModel.getId());
            if (commodityInfo == null){
                return ResultData.returnResultData(-1,null);
            }
            return ResultData.returnResultData(1,commodityInfo);
        }
    }

    @Override
    public ResultData findByIds(Map<String,Object> map) {
        PageData pageData = new PageData();
        Integer pageIndex,pageSize;
        Integer isDelete = (Integer) map.get("isDelete");
        if (isDelete == null){
            map.put("isDelete",0);
        }
        pageIndex = (Integer) map.get("pageIndex");
        pageSize = (Integer) map.get("pageSize");
        if (pageIndex != null) {
            Pageable pageable;
            Sort sort = new Sort(Sort.Direction.DESC,"sortNumber");
            if (pageSize == null) {
                pageable = new PageRequest(pageIndex, 15, sort);
            }else{
                pageable = new PageRequest(pageIndex, pageSize, sort);
            }
            map.put("pageable",pageable);
            pageData.setPageable(pageable);
            pageData.setModelData(commodityInfoDao.selectCommodities(map));
        }else {
            pageData.setModelData(commodityInfoDao.selectCommodities(map));
        }
        return  ResultData.returnResultData(1,pageData);
    }

    @Override
    public ResultData sortCommodityInfo(FindModel findModel) {
        if (findModel == null || findModel.getId() == null){
            return ResultData.returnResultData(-1,null);
        }
        return  ResultData.returnResultData(commodityInfoDao.updateSortNumber(findModel.getId()),null);
    }

    @Override
    public ResultData discounts(Operate operate) {
        List ids = null;
        Integer isDiscount = null;
        if (operate != null){
            ids = operate.getIds();
            isDiscount =operate.getType();
        }
        if (ids == null ||ids.size()<1 || isDiscount == null)
        {
            return ResultData.returnResultData(-1,null);
        }
        return  ResultData.returnResultData(commodityInfoDao.discounts(ids,isDiscount),null);
    }

    @Override
    public ResultData sales(Operate operate) {
        List ids = null;
        Integer isSales = null;
        if (operate != null){
            ids = operate.getIds();
            isSales =operate.getType();
        }
        if (ids == null ||ids.size()<1 || isSales == null)
        {
            return ResultData.returnResultData(-1,null);
        }
        return  ResultData.returnResultData(commodityInfoDao.sales(ids, isSales),null);
    }

}
