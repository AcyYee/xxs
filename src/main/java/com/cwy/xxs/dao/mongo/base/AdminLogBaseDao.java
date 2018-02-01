package com.cwy.xxs.dao.mongo.base;

import com.cwy.xxs.entity.AdminLog;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Acy
 */
public interface AdminLogBaseDao extends MongoRepository<AdminLog,String> {
}
