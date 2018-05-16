package com.cwy.xxs.dao.mongo.plus.impl;

import com.cwy.xxs.dao.mongo.plus.PersonInfoDao;
import com.cwy.xxs.entity.PersonInfo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * @author acy19
 */
@Repository
public class PersonInfoDaoImpl implements PersonInfoDao {

    private final MongoTemplate mongoTemplate;

    public PersonInfoDaoImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public int updateDefaultAddress(String id, String addressId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("addressId", addressId);
        return mongoTemplate.updateFirst(query, update, PersonInfo.class).getN();
    }

}
