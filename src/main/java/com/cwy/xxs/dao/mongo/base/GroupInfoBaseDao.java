package com.cwy.xxs.dao.mongo.base;

import com.cwy.xxs.entity.GroupInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author acy19
 */
public interface GroupInfoBaseDao extends MongoRepository<GroupInfo,String> {
}
