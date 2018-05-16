package com.cwy.xxs.dao.mongo.plus.impl;

import com.cwy.xxs.dao.mongo.plus.GroupInfoDao;
import com.cwy.xxs.entity.GroupInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author acy19
 */

@Repository
public class GroupInfoDaoImpl implements GroupInfoDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public GroupInfoDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void updateGroupNumber(String groupId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(groupId));
        Update update = new Update();
        update.inc("groupNumber",1);
        mongoTemplate.updateMulti(query,update,GroupInfo.class);
    }

    @Override
    public int deleteByIds(List ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(ids));
        Update update = new Update();
        update.set("isDelete",1);
        return mongoTemplate.updateMulti(query,update,GroupInfo.class).getN();
    }

    @Override
    public List<GroupInfo> findGroupInfoes(Map<String, Object> map) {
        Query query = new Query();
        if (map.get("personId") != null){
            query.addCriteria(new Criteria("personId").is(map.get("personId")));
        }
        if (map.get("groupType") != null){
            query.addCriteria(new Criteria("groupType").is(map.get("groupType")));
        }
        if (map.get("pageable") != null){
            query.with((Pageable) map.get("pageable"));
        }
        query.addCriteria(Criteria.where("isDelete").is(0));
        return mongoTemplate.find(query,GroupInfo.class);
    }
}
