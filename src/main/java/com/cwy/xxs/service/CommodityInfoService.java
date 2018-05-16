package com.cwy.xxs.service;

import com.cwy.xxs.entity.CommodityInfo;
import com.cwy.xxs.vo.FindModel;
import com.cwy.xxs.vo.Operate;
import com.cwy.xxs.vo.ResultData;

import java.util.Map;

public interface CommodityInfoService {

    ResultData addCommodityInfo(CommodityInfo commodityInfo);

    ResultData updateCommodityInfo(CommodityInfo commodityInfo);

    ResultData deletesCommodityInfo(Operate operate);

    ResultData findById(FindModel findModel);

    ResultData findByIds(Map<String,Object> map);

    ResultData sortCommodityInfo(FindModel findModel);

    ResultData discounts(Operate operate);

    ResultData sales(Operate operate);


}
