package com.cwy.xxs.dao.mongo.base;

import com.cwy.xxs.entity.StoreInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StoreInfoBaseDao extends MongoRepository<StoreInfo,String> {
}
