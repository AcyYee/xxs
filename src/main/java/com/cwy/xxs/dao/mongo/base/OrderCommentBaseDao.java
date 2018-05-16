package com.cwy.xxs.dao.mongo.base;

import com.cwy.xxs.entity.OrderComment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * @author acy19
 */
public interface OrderCommentBaseDao extends MongoRepository<OrderComment,String> {

    @Query(value = "{\"itemId\":?0 ,\"isDelete\" : 0}")
    OrderComment findByItemId(Integer itemId);
}
