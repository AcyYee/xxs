package com.cwy.xxs.dao.mongo.base;

import com.cwy.xxs.entity.GroupItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GroupItemBaseDao extends MongoRepository<GroupItem,String>{

    @Query(value = "{\"isDelete\":0,\"groupId\":?0}")
    List<GroupItem> findByGroupId(String groupId);

    @Query(value = "{\"isDelete\":0,\"personId\":?0,\"groupId\":?1}" ,count = true)
    Integer findByPersonIdAndGroupId(String personId, String id);
}
