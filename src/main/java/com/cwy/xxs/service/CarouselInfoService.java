package com.cwy.xxs.service;

import com.cwy.xxs.entity.CarouselInfo;
import com.cwy.xxs.vo.ResultData;

import java.util.List;
import java.util.Map;

/**
 * @author acy19
 */
public interface CarouselInfoService {

    ResultData getCarouselInfo(String carouselId);

    ResultData getCarouselInfos(Map<String, Object> map);

    ResultData deleteByIds(List carouselIds);

    ResultData updateCarouselInfo(CarouselInfo carouselInfo);

    ResultData addCarouselInfo(CarouselInfo carouselInfo);
}
