package com.cwy.xxs.dao.mongo.base;

import com.cwy.xxs.entity.CommodityCategory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommodityCategoryBaseDao extends MongoRepository<CommodityCategory,String> {
}
