package com.cwy.xxs.dao.mongo.plus.impl;


import com.cwy.xxs.dao.mongo.plus.CarouselInfoDao;
import com.cwy.xxs.entity.CarouselInfo;
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

import java.util.List;
import java.util.Map;

@Repository
public class CarouselInfoDaoImpl implements CarouselInfoDao {

    private final MongoTemplate mongoTemplate;

    private final Logger logger = LoggerFactory.getLogger(CarouselInfoDaoImpl.class);
    @Autowired
    public CarouselInfoDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public int deleteByIds(List ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(ids));
        Update update = new Update();
        update.set("isDelete",1);
        return mongoTemplate.updateMulti(query,update,CarouselInfo.class).getN();
    }

    @Override
    public int updateSortNumber(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.inc("sortNumber",1);
        return mongoTemplate.updateMulti(query,update,CarouselInfo.class).getN();
    }

    @Override
    public int update(CarouselInfo carouselInfo) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(carouselInfo.getId()));
        Update update = MongoUtil.getUpdate(carouselInfo);
        return mongoTemplate.updateMulti(query,update,CarouselInfo.class).getN();
    }

    @Override
    public int findCount(Map<String, Object> map) {
        Query query = new Query();
        if (map.get("commodityId") != null){
            query.addCriteria(Criteria.where("commodityId").is(map.get("commodityId")));
        }
        if (map.get("carouselType") != null){
            query.addCriteria(new Criteria("carouselType").is(map.get("carouselType")));
        }
        query.addCriteria(Criteria.where("isDelete").is(0));
        return (int) mongoTemplate.count(query,CarouselInfo.class);
    }

    @Override
    public List<CarouselInfo> findCarouselInfoes(Map<String, Object> map) {
        Query query = new Query();
        if (map.get("commodityId") != null){
            query.addCriteria(Criteria.where("commodityId").is(map.get("commodityId")));
        }
        if (map.get("carouselType") != null){
            query.addCriteria(new Criteria("carouselType").is(map.get("carouselType")));
        }
        if (map.get("pageable") != null){
            query.with((Pageable) map.get("pageable"));
        }
        query.addCriteria(Criteria.where("isDelete").is(0));
        logger.info(query.toString());
        return mongoTemplate.find(query,CarouselInfo.class);
    }

}
