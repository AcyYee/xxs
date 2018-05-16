package com.cwy.xxs.dao.mongo.plus.impl;

import com.cwy.xxs.dao.mongo.plus.GroupItemDao;
import com.cwy.xxs.entity.GroupInfo;
import com.cwy.xxs.entity.GroupItem;
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
public class GroupItemDaoImpl implements GroupItemDao {

    private final MongoTemplate mongoTemplate;

    public GroupItemDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<GroupItem> findGroupItems(Map<String, Object> map) {
        Query query = new Query();
        if (map.get("personId") != null){
            query.addCriteria(new Criteria("personId").is(map.get("personId")));
        }
        if (map.get("itemType") != null){
            query.addCriteria(new Criteria("itemType").is(map.get("itemType")));
        }
        if (map.get("pageable") != null){
            query.with((Pageable) map.get("pageable"));
        }
        query.addCriteria(Criteria.where("isDelete").is(0));
        return mongoTemplate.find(query,GroupItem.class);
    }

    @Override
    public void updateTypeByGroupId(String groupId, String dateTime,int type) {
        Query query = new Query();
        query.addCriteria(Criteria.where("groupId").is(groupId));
        Update update = new Update();
        update.set("itemType",type);
        update.set("updateTime",dateTime);
        mongoTemplate.updateMulti(query,update,GroupItem.class);
    }
}
