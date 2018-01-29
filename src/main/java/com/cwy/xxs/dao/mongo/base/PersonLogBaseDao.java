package com.cwy.xxs.dao.mongo.base;

import com.cwy.xxs.entity.PersonLog;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author acy19
 * @version 1.0
 * 完成简单容易的增删改查
 */
public interface PersonLogBaseDao extends MongoRepository<PersonLog,String> {

}
