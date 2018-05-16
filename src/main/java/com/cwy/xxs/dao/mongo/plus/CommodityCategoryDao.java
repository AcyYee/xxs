package com.cwy.xxs.dao.mongo.plus;

import com.cwy.xxs.entity.CommodityCategory;

import java.util.List;
import java.util.Map;

public interface CommodityCategoryDao {
    int deleteByIds(List ids);

    int updateSortNumber(String id);

    int update(CommodityCategory commodityCategory);

    int findCategoryCount(Map<String, Object> map);

    List<CommodityCategory> findCategories(Map<String, Object> map);
}
