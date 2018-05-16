package com.cwy.xxs.service;

import com.cwy.xxs.entity.GroupInfo;
import com.cwy.xxs.vo.FindModel;
import com.cwy.xxs.vo.Operate;
import com.cwy.xxs.vo.ResultData;

import java.util.Map;

/**
 * @author acy19
 */
public interface GroupInfoService {

    /**
     * 查找单个团购信息
     * @param findModel id
     * @return 返回数据
     */
    ResultData findGroupInfo(FindModel findModel);

    /**
     * 查找多个团购信息
     * @param map 查询多个
     * @return 返回数据
     */
    ResultData findGroupInfoes(Map<String,Object> map);

    /**
     * 删除多个团购信息
     * @param operate 团购信息
     * @return 返回数据
     */
    ResultData deleteGroupInfoes(Operate operate);

    ResultData findGroupInfoModel(FindModel findModel);

}
