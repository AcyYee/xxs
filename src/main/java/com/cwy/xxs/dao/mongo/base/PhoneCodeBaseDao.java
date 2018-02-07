package com.cwy.xxs.dao.mongo.base;

import com.cwy.xxs.entity.PhoneCode;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * @author Acy
 */
public interface PhoneCodeBaseDao extends MongoRepository<PhoneCode,String> {

    /**
     * 验证手机
     * @param userPhone 手机号
     * @param phoneCode 验证码
     * @param dateTime 验证时间
     * @param type 验证码类型
     * @param sort 排序
     * @return 返回记录
     */
    @Query(value = "{\"phoneNumber\":?0,\"codeTag\":?1,\"isUsed\":0,\"isDelete\":0,\"codeType\":?3,\"endTime\":{\"$gt\":?2},\"createTime\":{\"$lt\":?2}}")
    PhoneCode findByPhoneAndCode(String userPhone, String phoneCode, String dateTime ,Integer type,Sort sort);

    /**
     * 验证手机
     * @param personId 用户
     * @param phoneCode 验证码
     * @param dateTime 验证时间
     * @param type 验证码类型
     * @param sort 排序
     * @return 返回记录
     */
    @Query(value = "{\"personId\":?0,\"codeTag\":?1,\"isUsed\":0,\"isDelete\":0,\"codeType\":?3,\"endTime\":{\"$gt\":?2},\"createTime\":{\"$lt\":?2}}")
    PhoneCode findByPersonAndCode(String personId, String phoneCode, String dateTime,Integer type ,Sort sort);

}
