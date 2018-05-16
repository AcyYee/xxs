package com.cwy.xxs.dao.mongo.base;

import com.cwy.xxs.entity.CommodityInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author acy19
 * @version 1.0
 */
public interface CommodityInfoBaseDao extends MongoRepository<CommodityInfo,String> {


}
