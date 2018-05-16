package com.cwy.xxs.service;

import com.cwy.xxs.entity.CommoditySpecification;
import com.cwy.xxs.vo.Operate;
import com.cwy.xxs.vo.PageData;

import java.util.Map;

public interface CommoditySpecificationService {

    int addSpecification(CommoditySpecification commoditySpecification);

    int updateSpecification(CommoditySpecification commoditySpecification);

    PageData getSpecifications(Map<String, Object> map);

    CommoditySpecification getSpecification(Integer specificationId);

    int deleteSpecifications(Operate operate);

    int topCommoditySpecification(Integer specificationId, Integer sortNumber);

    int sortCommoditySpecification(Integer specificationId, Integer sortNumber);

}
