package com.cwy.xxs.dao.mongo.base;

import com.cwy.xxs.entity.GroupBuy;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author acy19
 */
public interface GroupBuyBaseDao extends MongoRepository<GroupBuy,String> {

}
