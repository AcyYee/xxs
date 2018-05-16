package com.cwy.xxs.service.impl;

import com.cwy.xxs.dao.mybatis.CommoditySpecificationMapper;
import com.cwy.xxs.entity.CommoditySpecification;
import com.cwy.xxs.service.CommoditySpecificationService;
import com.cwy.xxs.util.TimeUtil;
import com.cwy.xxs.vo.Operate;
import com.cwy.xxs.vo.PageData;
import com.cwy.xxs.vo.PageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author acy 屋大维
 */
@Service("commoditySpecificationService")
public class CommoditySpecificationServiceImpl implements CommoditySpecificationService {

    private final CommoditySpecificationMapper commoditySpecificationDao;

    @Autowired
    public CommoditySpecificationServiceImpl(CommoditySpecificationMapper commoditySpecificationDao) {
        this.commoditySpecificationDao = commoditySpecificationDao;
    }

    @Override
    public int addSpecification(CommoditySpecification commoditySpecification) {
        if (commoditySpecification == null || commoditySpecification.notEmpty()) {
            return -1;
        }
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        commoditySpecification.setCreateTime(dateTime);
        commoditySpecification.setUpdateTime(dateTime);
        commoditySpecification.setSpecificationWeight(0);
        commoditySpecification.setActivityPrice(0.0);
        commoditySpecification.setIsDelete(0);
        if (commoditySpecificationDao.insertSelective(commoditySpecification)>0){
            return commoditySpecificationDao.updateSortNumber(commoditySpecification.getSpecificationId(),commoditySpecification.getSpecificationId());
        }else {
            return 0;
        }
    }

    @Override
    public int updateSpecification(CommoditySpecification commoditySpecification) {
        if (commoditySpecification == null ||commoditySpecification.getSpecificationId()==null) {
            return -1;
        }
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        commoditySpecification.setUpdateTime(dateTime);
        return commoditySpecificationDao.updateByPrimaryKeySelective(commoditySpecification);
    }

    @Override
    public PageData getSpecifications(Map<String, Object> map){
        PageData pageData = new PageData();
        Integer pageIndex = (Integer) map.get("pageIndex");
        if (pageIndex != null){
            PageModel pageModel = new PageModel();
            pageModel.setPageIndex(pageIndex);
            pageModel.setRecordCount(commoditySpecificationDao.selectSpecificationCount(map));
            map.put("pageModel",pageModel);
            pageData.setPageModel(pageModel);
            pageData.setModelData(commoditySpecificationDao.selectSpecifications(map));
            return pageData;
        }else {
            pageData.setModelData(commoditySpecificationDao.selectSpecifications(map));
            return pageData;
        }
    }

    @Override
    public CommoditySpecification getSpecification(Integer specificationId) {
        if (specificationId == null){
            return null;
        }
        return commoditySpecificationDao.selectByPrimaryKey(specificationId);
    }

    @Override
    public int deleteSpecifications(Operate operate) {
        if (operate == null || operate.getIds() == null || operate.getIds().size() <1) {
            return -1;
        }
        return commoditySpecificationDao.deleteByIds(operate.getIds());
    }

    @Override
    public int topCommoditySpecification(Integer specificationId, Integer sortNumber) {
        if (specificationId == null || sortNumber==null) {
            return -1;
        }
        commoditySpecificationDao.updateSortNumbers(sortNumber);
        return commoditySpecificationDao.updateSortNumber(specificationId,1);
    }

    @Override
    public int sortCommoditySpecification(Integer specificationId, Integer sortNumber) {
        if (specificationId == null || sortNumber==null) {
            return -1;
        }
        return commoditySpecificationDao.updateSortNumber(specificationId, sortNumber);
    }
}