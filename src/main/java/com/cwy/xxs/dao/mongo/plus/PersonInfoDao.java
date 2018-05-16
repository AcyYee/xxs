package com.cwy.xxs.dao.mongo.plus;

/**
 * @author acy19
 */
public interface PersonInfoDao {


    /**
     * @param id 用户id
     * @param addressId 地址id
     * @return 返回处理结果
     */
    int updateDefaultAddress(String id, String addressId);

}
