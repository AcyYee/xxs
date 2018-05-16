package com.cwy.xxs.service;

import com.cwy.xxs.entity.GroupBuy;
import com.cwy.xxs.vo.FindModel;
import com.cwy.xxs.vo.Operate;
import com.cwy.xxs.vo.ResultData;

import java.util.Map;

public interface GroupBuyService {
    ResultData addGroupBuy(GroupBuy groupBuy);

    ResultData updateGroupBuy(GroupBuy groupBuy);

    ResultData deleteGroupBuies(Operate operate);

    ResultData findGroupBuy(FindModel findModel);

    ResultData findGroupBuies(Map<String, Object> map);

    ResultData useGroupBuies(Operate operate);
}
