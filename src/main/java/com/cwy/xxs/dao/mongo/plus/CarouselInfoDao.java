package com.cwy.xxs.dao.mongo.plus;

import com.cwy.xxs.entity.CarouselInfo;

import java.util.List;
import java.util.Map;

public interface CarouselInfoDao {
    int deleteByIds(List ids);

    int updateSortNumber(String id);

    int update(CarouselInfo carouselInfo);

    int findCount(Map<String, Object> map);

    List<CarouselInfo> findCarouselInfoes(Map<String, Object> map);
}
