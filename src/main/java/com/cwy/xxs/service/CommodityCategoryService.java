package com.cwy.xxs.service;

import com.cwy.xxs.entity.CommodityCategory;
import com.cwy.xxs.vo.FindModel;
import com.cwy.xxs.vo.ResultData;

import java.util.List;
import java.util.Map;

public interface CommodityCategoryService {

    ResultData getCategorys(Map<String,Object> map);


    ResultData findCategory(FindModel findModel);

    ResultData removeCategorys(List ids);

    ResultData updateCategory(CommodityCategory commodityCategory);

    ResultData topCategory(String id);

    ResultData insertCategory(CommodityCategory commodityCategory);

    ResultData sortCategory(FindModel findModel);
}
