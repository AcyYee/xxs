package com.cwy.xxs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Acy
 */

@Document(collection = "admin_info_acy")
@Data
public class AdminInfo {

    @Id
    private String id;

    private String adminName,adminPassword,lastLoginTime,lastLoginIP,createTime,updateTime;

    private Integer adminType,isDelete;

}
