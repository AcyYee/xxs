package com.cwy.xxs.service.impl;

import com.cwy.xxs.dao.mongo.base.CarouselInfoBaseDao;
import com.cwy.xxs.dao.mongo.plus.CarouselInfoDao;
import com.cwy.xxs.entity.CarouselInfo;
import com.cwy.xxs.service.CarouselInfoService;
import com.cwy.xxs.util.TimeUtil;
import com.cwy.xxs.vo.PageData;
import com.cwy.xxs.vo.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author acy19
 */
@Service
public class CarouselInfoServiceImpl  implements CarouselInfoService{

    private final CarouselInfoDao carouselInfoDao;

    private final CarouselInfoBaseDao carouselInfoBaseDao;

    public CarouselInfoServiceImpl(CarouselInfoDao carouselInfoDao, CarouselInfoBaseDao carouselInfoBaseDao) {
        this.carouselInfoDao = carouselInfoDao;
        this.carouselInfoBaseDao = carouselInfoBaseDao;
    }

    @Override
    public ResultData getCarouselInfo(String carouselId) {
        if (carouselId == null || "".equals(carouselId)){
            return ResultData.returnResultData(-1,null);
        }
        CarouselInfo carouselInfo = carouselInfoBaseDao.findOne(carouselId);
        if (carouselInfo == null || carouselInfo.getId()  == null){
            return ResultData.returnResultData(0,null);
        }
        return ResultData.returnResultData(1,carouselInfo);
    }

    @Override
    public ResultData getCarouselInfos(Map<String, Object> map) {
        Integer carouselType = (Integer) map.get("carouselType");
        if (carouselType == null ){
            return ResultData.returnResultData(-1,null);
        }
        PageData pageData = new PageData();
        pageData.setModelData(carouselInfoDao.findCarouselInfoes(map));
        return ResultData.returnResultData(1,pageData);
    }

    @Override
    public ResultData deleteByIds(List carouselIds) {
        if (carouselIds == null || carouselIds.size()<1){
            return ResultData.returnResultData(-1,null);
        }
        return ResultData.returnResultData(carouselInfoDao.deleteByIds(carouselIds),carouselIds);
    }

    @Override
    public ResultData updateCarouselInfo(CarouselInfo carouselInfo) {
        if (carouselInfo == null || carouselInfo.notEmpty()){
            return ResultData.returnResultData(-1,null);
        }
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        carouselInfo.setUpdateTime(dateTime);
        return ResultData.returnResultData(carouselInfoDao.update(carouselInfo),carouselInfoBaseDao.findOne(carouselInfo.getId()));
    }

    @Override
    public ResultData addCarouselInfo(CarouselInfo carouselInfo) {
        if (carouselInfo ==null || carouselInfo.empty()){
            return ResultData.returnResultData(-1,null);
        }
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        carouselInfo.setStoreId("1");
        carouselInfo.setCreateTime(dateTime);
        carouselInfo.setUpdateTime(dateTime);
        carouselInfo.setIsDelete(0);
        carouselInfo = carouselInfoBaseDao.save(carouselInfo);
        if (carouselInfo == null || carouselInfo.getId() == null || "".equals(carouselInfo.getId())){
            return ResultData.returnResultData(0,carouselInfo);
        }else{
            return ResultData.returnResultData(1,carouselInfo);
        }
    }
}
