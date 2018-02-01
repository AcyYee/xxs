package com.cwy.xxs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author acy19
 */
@Data
@Document(collection = "person_info_acy")
public class PersonInfo {

    @Id
    private String id;

    private String createTime,wxGender,wxProvince,wxCity,wxCountry,lastLoginIP,updateTime,lastLoginTime,mobilePhone,userAddress,wxNico,wxIcon,realName,openid,sessionKey;

    private Integer isDelete,vipLevel;

}
