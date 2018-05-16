package com.cwy.xxs.dao.mongo.base;

import com.cwy.xxs.entity.UserShopCar;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserShopCarBaseDao extends MongoRepository<UserShopCar,String> {
    /**
     *  获取购物车
     * @param id 用户id
     * @return 返回购物车
     */
    UserShopCar findByPersonId(String id);
}
