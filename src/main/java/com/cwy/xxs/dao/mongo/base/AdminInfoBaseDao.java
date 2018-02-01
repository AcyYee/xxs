package com.cwy.xxs.dao.mongo.base;

import com.cwy.xxs.entity.AdminInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * @author Acy
 */
public interface AdminInfoBaseDao extends MongoRepository<AdminInfo,String> {

    @Query(value = "{\"adminName\":?0,\"adminPassword\":?1,\"isDelete\":0}",fields = "{\"id\":1,\"adminName\":1,\"adminType\":1}")
    AdminInfo findByNameAndPassword(String adminName, String adminPassword);
}
