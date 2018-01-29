package com.cwy.xxs.dao.mongo.base;

import com.cwy.xxs.entity.PersonInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * @author acy19
 */
public interface PersonInfoBaseDao extends MongoRepository<PersonInfo,String> {
    @Query(value = "{\"openid\": ? }")
    PersonInfo findByOpenid(String openid);

}
