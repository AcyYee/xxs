package com.cwy.xxs.dao.mongo.plus;

import com.cwy.xxs.entity.OrderComment;

import java.util.List;
import java.util.Map;

public interface OrderCommentDao {
    int deleteByIds(List ids);

    List<OrderComment> findOrderComments(Map<String, Object> map);
}
