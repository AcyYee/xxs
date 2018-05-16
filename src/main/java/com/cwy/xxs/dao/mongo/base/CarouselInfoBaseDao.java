package com.cwy.xxs.dao.mongo.base;

import com.cwy.xxs.entity.CarouselInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author acy19
 */
public interface CarouselInfoBaseDao extends MongoRepository<CarouselInfo,String> {
}
