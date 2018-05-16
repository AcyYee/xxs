package com.cwy.xxs.dao.mongo.plus;

import com.cwy.xxs.entity.GroupBuy;
import com.cwy.xxs.vo.Operate;

import java.util.List;
import java.util.Map;

/**
 * @author acy19
 */
public interface GroupBuyDao {

    int deleteBuIds(List ids);

    int update(GroupBuy groupBuy);

    List<GroupBuy> selectGroupBuies(Map<String, Object> map);

    int useBuIds(Operate operate);
}
