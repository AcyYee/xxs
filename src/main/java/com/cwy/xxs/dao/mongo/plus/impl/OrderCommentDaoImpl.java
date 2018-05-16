package com.cwy.xxs.dao.mongo.plus.impl;

import com.cwy.xxs.dao.mongo.plus.OrderCommentDao;
import com.cwy.xxs.entity.OrderComment;
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
public class OrderCommentDaoImpl implements OrderCommentDao {

    private final MongoTemplate mongoTemplate;

    public OrderCommentDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public int deleteByIds(List ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(ids));
        Update update = new Update();
        update.set("isDelete",1);
        return mongoTemplate.updateMulti(query,update,OrderComment.class).getN();
    }

    @Override
    public List<OrderComment> findOrderComments(Map<String, Object> map) {
        Query query = new Query();
        if (map.get("commodityId") != null){
            query.addCriteria(new Criteria("commodityId").is(map.get("commodityId")));
        }
        if (map.get("orderId") != null){
            query.addCriteria(new Criteria("orderId").is(map.get("orderId")));
        }
        if (map.get("pageable") != null){
            query.with((Pageable) map.get("pageable"));
        }
        query.addCriteria(Criteria.where("isDelete").is(0));
        return mongoTemplate.find(query,OrderComment.class);
    }
}
