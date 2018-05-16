package com.cwy.xxs.dao.mongo.plus.impl;

import com.cwy.xxs.dao.mongo.plus.GroupBuyDao;
import com.cwy.xxs.entity.GroupBuy;
import com.cwy.xxs.util.MongoUtil;
import com.cwy.xxs.vo.Operate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class GroupBuyDaoImpl implements GroupBuyDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public GroupBuyDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public int update(GroupBuy groupBuy) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(groupBuy.getId()));
        Update update = MongoUtil.getUpdate(groupBuy);
        return mongoTemplate.updateMulti(query,update,GroupBuy.class).getN();
    }

    @Override
    public List<GroupBuy> selectGroupBuies(Map<String, Object> map) {
        Integer isUsed = (Integer) map.get("isUsed");
        Integer buyType = (Integer) map.get("buyType");
        String searchString = (String) map.get("searchString");
        Integer specificationId = (Integer) map.get("specificationId");
        String commodityId = (String) map.get("commodityId");
        Query query = new Query();
        if (isUsed != null){
            query.addCriteria(Criteria.where("isUsed").is(isUsed));
        }
        if (buyType != null){
            query.addCriteria(Criteria.where("buyType").is(buyType));
        }
        if (specificationId != null){
            query.addCriteria(Criteria.where("specificationId").is(specificationId));
        }
        if (commodityId != null){
            query.addCriteria(Criteria.where("commodityId").is(commodityId));
        }
        if (searchString != null && !"".equals(searchString.trim())){
            query.addCriteria(new Criteria("groupName").regex(searchString));
        }
        if (map.get("pageable") != null){
            query.with((Pageable) map.get("pageable"));
        }
        query.addCriteria(Criteria.where("isDelete").is(0));
        return mongoTemplate.find(query,GroupBuy.class);
    }

    @Override
    public int deleteBuIds(List ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(ids));
        Update update = new Update();
        update.set("isDelete",1);
        return mongoTemplate.updateMulti(query,update,GroupBuy.class).getN();
    }

    @Override
    public int useBuIds(Operate operate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(operate.getIds()));
        Update update = new Update();
        update.set("isUsed",operate.getType());
        return mongoTemplate.updateMulti(query,update,GroupBuy.class).getN();
    }
}
