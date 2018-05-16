package com.cwy.xxs.dao.mongo.plus.impl;

import com.cwy.xxs.dao.mongo.plus.CommodityCategoryDao;
import com.cwy.xxs.entity.CommodityCategory;
import com.cwy.xxs.util.MongoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Repository
public class CommodityCategoryDaoImpl implements CommodityCategoryDao {

    private final MongoTemplate mongoTemplate;

    private final Logger logger = LoggerFactory.getLogger(CommodityCategoryDaoImpl.class);
    @Autowired
    public CommodityCategoryDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public int deleteByIds(List ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(ids));
        Update update = new Update();
        update.set("isDelete",1);
        return mongoTemplate.updateMulti(query,update,CommodityCategory.class).getN();
    }

    @Override
    public int updateSortNumber(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.inc("sortNumber",1);
        return mongoTemplate.updateMulti(query,update,CommodityCategory.class).getN();
    }

    @Override
    public int update(CommodityCategory commodityCategory) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(commodityCategory.getId()));
        Update update = MongoUtil.getUpdate(commodityCategory);
        return mongoTemplate.updateMulti(query,update,CommodityCategory.class).getN();
    }

    @Override
    public int findCategoryCount(Map<String, Object> map) {
        Query query = new Query();
        if (map.get("searchString") != null){
            query.addCriteria(new Criteria().orOperator(new Criteria("categoryName").regex((String) map.get("searchString")),new Criteria("subName").regex((String) map.get("searchString"))));
        }
        if (map.get("storeId") != null){
            query.addCriteria(new Criteria("storeId").is((String) map.get("storeId")));
        }
        query.addCriteria(Criteria.where("isDelete").is(0));
        return (int) mongoTemplate.count(query,CommodityCategory.class);
    }

    @Override
    public List<CommodityCategory> findCategories(Map<String, Object> map) {
        Query query = new Query();
        if (map.get("searchString") != null){
            query.addCriteria(new Criteria().orOperator(new Criteria("categoryName").regex((String) map.get("searchString")),new Criteria("subName").regex((String) map.get("searchString"))));
        }
        if (map.get("storeId") != null){
            query.addCriteria(new Criteria("storeId").is((String) map.get("storeId")));
        }
        if (map.get("pageable") != null){
            query.with((Pageable) map.get("pageable"));
        }
        query.addCriteria(Criteria.where("isDelete").is(0));
        logger.info(query.toString());
        return mongoTemplate.find(query,CommodityCategory.class);
    }
}
