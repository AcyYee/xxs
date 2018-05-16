package com.cwy.xxs.dao.mongo.plus.impl;

import com.cwy.xxs.dao.mongo.plus.CommodityInfoDao;
import com.cwy.xxs.entity.CommodityInfo;
import com.cwy.xxs.util.MongoUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author acy19
 */
@Repository
public class CommodityInfoDaoImpl implements CommodityInfoDao {

    private final MongoTemplate mongoTemplate;

    public CommodityInfoDaoImpl(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public int deleteByIds(List ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(ids));
        Update update = new Update();
        update.set("isDelete",1);
        return mongoTemplate.updateMulti(query,update,CommodityInfo.class).getN();
    }

    @Override
    public int selectCommodityCount(Map<String, Object> map) {
        Query query = new Query();
        if (map.get("searchString") != null){
            query.addCriteria(new Criteria().orOperator(new Criteria("commodityName").regex((String) map.get("searchString")),new Criteria("subName").regex((String) map.get("searchString"))));
        }
        if (map.get("storeId") != null){
            query.addCriteria(new Criteria("storeId").is((String) map.get("storeId")));
        }
        if (map.get("categoryId") != null){
            query.addCriteria(new Criteria("categoryId").is((String) map.get("categoryId")));
        }
        if (map.get("isSales") != null){
            query.addCriteria(new Criteria("isSales").is((Integer) map.get("isSales")));
        }
        query.addCriteria(Criteria.where("isDelete").is(0));
        return (int) mongoTemplate.count(query,CommodityInfo.class);
    }

    @Override
    public List<CommodityInfo> selectCommodities(Map<String, Object> map) {
        Query query = new Query();
        if (map.get("searchString") != null){
            query.addCriteria(new Criteria().orOperator(new Criteria("commodityName").regex((String) map.get("searchString")),new Criteria("subName").regex((String) map.get("searchString"))));
        }
        if (map.get("storeId") != null){
            query.addCriteria(new Criteria("storeId").is(map.get("storeId")));
        }
        if (map.get("categoryId") != null){
            query.addCriteria(new Criteria("categoryId").is(map.get("categoryId")));
        }
        if (map.get("isSales") != null){
            query.addCriteria(new Criteria("isSales").is(map.get("isSales")));
        }
        if (map.get("isGroup") != null){
            query.addCriteria(new Criteria("isGroup").gte(map.get("isGroup")));
        }
        if (map.get("pageable") != null){
            query.with((Pageable) map.get("pageable"));
        }
        query.addCriteria(Criteria.where("isDelete").is(0));
        Field field = query.fields();
        field.include("commodityName");
        field.include("isSales");
        field.include("commodityPrice");
        field.include("subName");
        field.include("imagePath");
        field.include("isGroup");
        return mongoTemplate.find(query,CommodityInfo.class);
    }

    @Override
    public int updateSortNumber(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        Update update = new Update();
        update.inc("sortNumber",1);
        return mongoTemplate.updateMulti(query,update,CommodityInfo.class).getN();
    }

    @Override
    public int discounts(List ids, Integer isDiscount) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(ids));
        Update update = new Update();
        update.set("isDiscount",isDiscount);
        return mongoTemplate.updateMulti(query,update,CommodityInfo.class).getN();
    }

    @Override
    public int sales(List ids, Integer isSales) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(ids));
        Update update = new Update();
        update.set("isSales",isSales);
        return mongoTemplate.updateMulti(query,update,CommodityInfo.class).getN();
    }

    @Override
    public int update(CommodityInfo commodityInfo) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(commodityInfo.getId()));
        Update update = MongoUtil.getUpdate(commodityInfo);
        return mongoTemplate.updateMulti(query,update,commodityInfo.getClass()).getN();
    }

    @Override
    public CommodityInfo find(String id) {
        return mongoTemplate.findById(id,CommodityInfo.class);
    }


    @Override
    public void updateGroup(String commodityId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(commodityId));
        Update update = new Update();
        update.inc("isGroup",1);
        mongoTemplate.updateMulti(query,update,CommodityInfo.class).getN();
    }

    @Override
    public void updateGroups(List ids, int i) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(ids));
        Update update = new Update();
        update.inc("isGroup", i);
        mongoTemplate.updateMulti(query,update,CommodityInfo.class).getN();
    }
}
