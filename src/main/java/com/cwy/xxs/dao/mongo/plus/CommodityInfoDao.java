package com.cwy.xxs.dao.mongo.plus;

import com.cwy.xxs.entity.CommodityInfo;
import com.cwy.xxs.vo.ResultData;

import java.util.List;
import java.util.Map;

/**
 * @author acy19
 */
public interface CommodityInfoDao {

    int deleteByIds(List ids);

    int selectCommodityCount(Map<String, Object> map);

    List<CommodityInfo> selectCommodities(Map<String, Object> map);

    int updateSortNumber(String id);

    int discounts(List ids, Integer isDiscount);

    int sales(List ids, Integer isSales);

    int update(CommodityInfo commodityInfo);

    CommodityInfo find(String id);

    void updateGroup(String commodityId);

    void updateGroups(List ids, int i);
}
