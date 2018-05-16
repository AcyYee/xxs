package com.cwy.xxs.service.impl;

import com.cwy.xxs.dao.mongo.base.CommodityCategoryBaseDao;
import com.cwy.xxs.dao.mongo.plus.CommodityCategoryDao;
import com.cwy.xxs.entity.CommodityCategory;
import com.cwy.xxs.service.CommodityCategoryService;
import com.cwy.xxs.util.TimeUtil;
import com.cwy.xxs.vo.FindModel;
import com.cwy.xxs.vo.PageData;
import com.cwy.xxs.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("commodityCategoryService")
public class CommodityCategoryServiceImpl implements CommodityCategoryService {

    private final CommodityCategoryBaseDao commodityCategoryBaseDao;

    private final CommodityCategoryDao commodityCategoryDao;

    @Autowired
    public CommodityCategoryServiceImpl(CommodityCategoryBaseDao commodityCategoryBaseDao,CommodityCategoryDao commodityCategoryDao) {
        this.commodityCategoryBaseDao = commodityCategoryBaseDao;
        this.commodityCategoryDao = commodityCategoryDao;
    }

    @Override
    public ResultData getCategorys(Map<String,Object> map) {
        PageData pageData = new PageData();
        Integer pageIndex = (Integer) map.get("pageIndex");
        Integer pageSize = (Integer) map.get("pageSize");
        if (pageIndex != null){
            Pageable pageable;
            if (pageSize == null) {
                pageable = new PageRequest(pageIndex, 15);
            }else{
                pageable = new PageRequest(pageIndex, pageSize);
            }
            map.put("pageable",pageable);
            pageData.setPageable(pageable);
            pageData.setModelData(commodityCategoryDao.findCategories(map));
            return ResultData.returnResultData(1,pageData);
        }
        pageData.setModelData(commodityCategoryDao.findCategories(map));
        return ResultData.returnResultData(1,pageData);
    }

    @Override
    public ResultData sortCategory(FindModel findModel) {
        if (findModel == null || findModel.getId() == null){
            return ResultData.returnResultData(-1,null);
        }
        return  ResultData.returnResultData(commodityCategoryDao.updateSortNumber(findModel.getId()),null);
    }

    @Override
    public ResultData findCategory(FindModel findModel) {
        if (findModel == null || findModel.getId() == null){
            return ResultData.returnResultData(-1,null);
        }
        CommodityCategory commodityCategory = commodityCategoryBaseDao.findOne(findModel.getId());
        if (commodityCategory == null){
            return ResultData.returnResultData(0,null);
        }
        return ResultData.returnResultData(1,commodityCategory);
    }

    @Override
    public ResultData removeCategorys(List ids) {
        if (ids == null || ids.size()<1){
            return ResultData.returnResultData(-1,null);
        }
        return ResultData.returnResultData(commodityCategoryDao.deleteByIds(ids),null);
    }

    @Override
    public ResultData updateCategory(CommodityCategory commodityCategory) {
        if (commodityCategory == null||commodityCategory.getId()==null){
            return ResultData.returnResultData(-1,null);
        }
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        commodityCategory.setUpdateTime(dateTime);
        return ResultData.returnResultData(commodityCategoryDao.update(commodityCategory),commodityCategoryBaseDao.findOne(commodityCategory.getId()));
    }

    @Override
    public ResultData topCategory(String id) {
        if (id == null){
            return ResultData.returnResultData(-1,null);
        }
        return ResultData.returnResultData(commodityCategoryDao.updateSortNumber(id),null);
    }

    @Override
    public ResultData insertCategory(CommodityCategory commodityCategory) {
        if (commodityCategory == null || commodityCategory.empty()){
            return ResultData.returnResultData(-1,null);
        }
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        commodityCategory.setCreateTime(dateTime);
        commodityCategory.setSortNumber(0);
        commodityCategory.setIsDelete(0);
        commodityCategory.setStoreId("1");
        commodityCategory = commodityCategoryBaseDao.save(commodityCategory);
        if (commodityCategory == null || commodityCategory.getId() == null){
            return ResultData.returnResultData(0,null);
        }else {
            return ResultData.returnResultData(1,commodityCategory);
        }
    }
}
