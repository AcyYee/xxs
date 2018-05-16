package com.cwy.xxs.dao.mongo.base;

import com.cwy.xxs.entity.PersonInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * @author acy19
 */
public interface PersonInfoBaseDao extends MongoRepository<PersonInfo,String> {
    @Query(value = "{\"openid\":?0}")
    PersonInfo findByOpenid(String openid);

    @Query(value = "{\"id\":?0}",fields = "{\"id\":1,\"vipLevel\":1,\"wxNico\":1,\"wxIcon\":1,\"wxGender\":1,\"wxProvince\":1,\"wxCity\":1}")
    PersonInfo findByIdOR(String openid);

    @Query(value = "{\"vipLevel\":{\"$gte\":1}}",fields = "{\"id\":1,\"realName\":1,\"mobilePhone\":1,\"userAddress\":1,\"vipLevel\":1,\"wxNico\":1,\"wxIcon\":1,\"wxGender\":1,\"wxProvince\":1,\"wxCity\":1}")
    List<PersonInfo> findByVipLevel(Pageable pageable);

    @Query(value = "{\"vipLevel\":{\"$gte\":1}}",fields = "{\"id\":1,\"realName\":1,\"mobilePhone\":1,\"userAddress\":1,\"vipLevel\":1,\"wxNico\":1,\"wxIcon\":1,\"wxGender\":1,\"wxProvince\":1,\"wxCity\":1}")
    List<PersonInfo> findByVipLevel();

    @Query(value = "{\"vipLevel\":{\"$gte\":1},\"$or\":[{\"wxNico\":{\"$regex\":?0,\"$options\":\"i\"}},{\"realName\":{\"$regex\":?0,\"$options\":\"i\"}},{\"mobilePhone\":{\"$regex\":?0,\"$options\":\"i\"}}]}",fields = "{\"id\":1,\"realName\":1,\"mobilePhone\":1,\"userAddress\":1,\"vipLevel\":1,\"wxNico\":1,\"wxIcon\":1,\"wxGender\":1,\"wxProvince\":1,\"wxCity\":1}")
    List<PersonInfo> findByVipLevel(String searchString, Pageable pageable);

    @Query(value = "{\"vipLevel\":{\"$gte\":1},\"$or\":[{\"wxNico\":{\"$regex\":?0,\"$options\":\"i\"}},{\"realName\":{\"$regex\":?0,\"$options\":\"i\"}},{\"mobilePhone\":{\"$regex\":?0,\"$options\":\"i\"}}]}",fields = "{\"id\":1,\"realName\":1,\"mobilePhone\":1,\"userAddress\":1,\"vipLevel\":1,\"wxNico\":1,\"wxIcon\":1,\"wxGender\":1,\"wxProvince\":1,\"wxCity\":1}")
    List<PersonInfo> findByVipLevel(String searchString);
}
